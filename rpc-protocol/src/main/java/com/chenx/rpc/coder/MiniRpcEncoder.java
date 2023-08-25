package com.chenx.rpc.coder;

import com.chenx.rpc.protocol.MiniRpcProtocol;
import com.chenx.rpc.protocol.MsgHeader;
import com.chenx.rpc.serialization.MiniRpcSerialization;
import com.chenx.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author chenx
 * @create 2023-08-23 17:39
 */
public class MiniRpcEncoder extends MessageToByteEncoder<MiniRpcProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MiniRpcProtocol<Object> msg, ByteBuf out) throws Exception {
        MsgHeader header = msg.getHeader();
//        根据自定义协议结构写入数据
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeByte(header.getSerialization());
        out.writeByte(header.getMsgType());
        out.writeByte(header.getStatus());
        out.writeLong(header.getRequestId());

        MiniRpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(header.getSerialization());
        byte[] data = rpcSerialization.serialize(msg.getBody());
        out.writeInt(data.length);      // 数据长度
        out.writeBytes(data);
    }
}
