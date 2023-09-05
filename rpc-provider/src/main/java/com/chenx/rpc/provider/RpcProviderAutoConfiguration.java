package com.chenx.rpc.provider;

import com.chenx.rpc.common.RpcProperties;
import com.chenx.rpc.provider.registry.RegistryFactory;
import com.chenx.rpc.provider.registry.RegistryService;
import com.chenx.rpc.provider.registry.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author chenx
 * @create 2023-08-22 15:19
 */
@Configuration
//通过下面这个注解将RpcProvider注入到容器中
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderAutoConfiguration {
    @Resource
    private RpcProperties rpcProperties;

    /**
     * 在容器中注册了一个RpcProvider实例，名称是init
     */
    @Bean
    public RpcProvider init() throws Exception {
//        通过配置文件中配置的属性信息获取注册类型 zk / eureka
        RegistryType registryType = RegistryType.valueOf(rpcProperties.getRegistryType());
//        根据注册类型，通过RegistryFactory获取注册服务实例
        RegistryService registryService = RegistryFactory.getInstance(this.rpcProperties.getRegistryAddr(), registryType);
        return new RpcProvider(this.rpcProperties.getServicePort(), registryService);
    }
}
