package com.chenx.rpc.consumer;

import com.chenx.rpc.common.MiniRpcFuture;
import com.chenx.rpc.common.MiniRpcRequest;
import com.chenx.rpc.common.MiniRpcRequestHolder;
import com.chenx.rpc.common.MiniRpcResponse;
import com.chenx.rpc.protocol.MiniRpcProtocol;
import com.chenx.rpc.protocol.MsgHeader;
import com.chenx.rpc.protocol.MsgType;
import com.chenx.rpc.protocol.ProtocolConstants;
import com.chenx.rpc.provider.registry.RegistryService;
import com.chenx.rpc.serialization.SerializationTypeEnum;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author chenx
 * @create 2023-08-29 15:01
 */
public class RpcInvokerProxy implements InvocationHandler {


    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;

    public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        构建rpc协议对象
        MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
        MsgHeader header = new MsgHeader();
        long requestId = MiniRpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setSerialization((byte) SerializationTypeEnum.HESSIAN.getType());
        header.setMsgType((byte) MsgType.RESPONSE.getType());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);

        MiniRpcRequest request = new MiniRpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();
        MiniRpcFuture<MiniRpcResponse> future = new MiniRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        MiniRpcRequestHolder.REQUEST_MAP.put(requestId, future);
        rpcConsumer.sendRequest(protocol, this.registryService);
//        todo: hold request by threadLocal

        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}
