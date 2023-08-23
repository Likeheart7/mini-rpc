package com.chenx.rpc.provider;

import com.chenx.rpc.common.RpcServiceHelper;
import com.chenx.rpc.common.ServiceMeta;
import com.chenx.rpc.provider.annotation.RpcService;
import com.chenx.rpc.provider.registry.RegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenx
 * @create 2023-08-22 16:18
 */
@Slf4j
public class RpcProvider implements InitializingBean, BeanPostProcessor {
//  ip地址
    private String serverAddress;
    private final int serverPort;
    private final RegistryService serviceRegistry;

//    存放服务初始化后所对应的bean，起到缓存的作用，在处理RPC请求时可以直接通过这个map拿到的对应的服务调用
    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public RpcProvider(int serverPort, RegistryService registryService){
        this.serverPort = serverPort;
        this.serviceRegistry = registryService;
    }


//    这个方法会在BeanPostProcessor#postProcessBeforeInitialization后调用
//            在BeanPostProcessor#postProcessAfterInitialization前调用
    @Override
    public void afterPropertiesSet() {
//        初始化bean的时候异步通过netty启动一个服务端
        new Thread(()->{
            try {
                startRpcServer();
            }catch (Exception e) {
                log.error("start rpc server error.", e);
            }
        }).start();
    }

    private void startRpcServer() throws Exception {
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast();
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture cf = bootStrap.bind(this.serverAddress, this.serverPort).sync();
            log.info("server address {} start on port {}", this.serverAddress, this.serverPort);
            cf.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 对所有初始化完成后的 Bean 进行处理，将所有被@RpcService修饰的bean放到map中
     * @param bean  当前处理的bean
     * @param beanName  当前处理的bean的名称
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if(rpcService != null) {
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceVersion();
            try {
                ServiceMeta serviceMeta = new ServiceMeta();
                serviceMeta.setServiceAddr(this.serverAddress);
                serviceMeta.setServicePort(this.serverPort);
                serviceMeta.setServiceName(serviceName);
                serviceMeta.setServiceVersion(serviceVersion);

                serviceRegistry.register(serviceMeta);
                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceName, serviceVersion), bean);
            } catch (Exception e) {
                log.error("fail to register service {}#{}", serviceName, serviceVersion, e);
            }
        }
        return bean;
    }
}
