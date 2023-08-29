package com.chenx.rpc.handler;

import com.chenx.rpc.common.MiniRpcRequest;
import com.chenx.rpc.common.MiniRpcResponse;
import com.chenx.rpc.common.RpcServiceHelper;
import com.chenx.rpc.protocol.MiniRpcProtocol;
import com.chenx.rpc.protocol.MsgHeader;
import com.chenx.rpc.protocol.MsgStatus;
import com.chenx.rpc.protocol.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;

/**
 * @author chenx
 * @create 2023-08-24 11:20
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<MiniRpcProtocol<MiniRpcRequest>> {
    private final Map<String, Object> rpcServiceMap;
    
    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MiniRpcProtocol<MiniRpcRequest> protocol) throws Exception {
//        因为rpc请求调用较慢，一般提交到自定义业务线程池中处理
        RpcRequestProcessor.submitRequest(() -> {
            MiniRpcProtocol<MiniRpcResponse> resProtocol = new MiniRpcProtocol<>();
            MiniRpcResponse response = new MiniRpcResponse();
            MsgHeader header = resProtocol.getHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getType());
            try {
                Object result = handle(protocol.getBody());
                response.setData(result);
                header.setStatus((byte) MsgStatus.SUCCESS.getCode());
                resProtocol.setHeader(header);
                resProtocol.setBody(response);
            } catch (Throwable t) {
                header.setStatus((byte) MsgStatus.FAIL.getCode());
                response.setMessage(t.toString());
                log.error("process request {} error", header.getRequestId(), t);
            }
            ctx.writeAndFlush(resProtocol);
        });
    }

//  通过request中的参数，从rpcServiceMap找到对应的bean，通过FastClass调用其方法
    private Object handle(MiniRpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] params = request.getParams();
        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, serviceBean, params);
    }
}
