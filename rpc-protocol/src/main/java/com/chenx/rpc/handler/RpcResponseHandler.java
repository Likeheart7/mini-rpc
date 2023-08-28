package com.chenx.rpc.handler;

import com.chenx.rpc.common.MiniRpcFuture;
import com.chenx.rpc.common.MiniRpcRequestHolder;
import com.chenx.rpc.common.MiniRpcResponse;
import com.chenx.rpc.protocol.MiniRpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chenx
 * @create 2023-08-28 10:32
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<MiniRpcProtocol<MiniRpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MiniRpcProtocol<MiniRpcResponse> msg) throws Exception {
        long requestId = msg.getHeader().getRequestId();
//        服务消费者在发起调用时，维护了请求 requestId 和 MiniRpcFuture<MiniRpcResponse> 的映射关系，
//        RpcResponseHandler 会根据请求的 requestId 找到对应发起调用的 MiniRpcFuture，然后为 MiniRpcFuture 设置响应结果。
        MiniRpcFuture<MiniRpcResponse> future = MiniRpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getBody());
    }

}
