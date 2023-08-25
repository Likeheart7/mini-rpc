package com.chenx.rpc.coder;

import com.chenx.rpc.common.MiniRpcRequest;
import com.chenx.rpc.common.MiniRpcResponse;
import com.chenx.rpc.protocol.MiniRpcProtocol;
import com.chenx.rpc.protocol.MsgHeader;
import com.chenx.rpc.protocol.MsgType;
import com.chenx.rpc.protocol.ProtocolConstants;
import com.chenx.rpc.serialization.MiniRpcSerialization;
import com.chenx.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定协议解码器
 *
 * @author chenx
 * @create 2023-08-23 17:39
 */
public class MiniRpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN)
            return;

        in.markReaderIndex();
        short magic = in.readShort();
//        根据魔数检测数据包是否符合规范
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }
        byte version = in.readByte();
        byte serialization = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        int msgLen = in.readInt();
        /**
         * 即使已经可以完整读取出协议头 Header，但是协议体 Body 有可能还未就绪。
         * 所以在刚开始读取数据时，需要使用 markReaderIndex() 方法标记读指针位置，
         * 当 ByteBuf 中可读字节长度小于协议体 Body 的长度时，再使用 resetReaderIndex() 还原读指针位置，
         * 说明现在 ByteBuf 中可读字节还不够一个完整的数据包。
         */
        if (in.readableBytes() < msgLen) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[msgLen];
        in.readBytes(data);
//        查看消息类型，如果类型不正确也直接结束
        MsgType msgTypeEnum = MsgType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }

        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serialization);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgLen(msgLen);

        MiniRpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(serialization);
//        根据报文类型决定如何处理消息体
        switch (msgTypeEnum) {
            case REQUEST:
                MiniRpcRequest request = rpcSerialization.deserialize(data, MiniRpcRequest.class);
                if (request != null) {
                    MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
            case RESPONSE:
                MiniRpcResponse response = rpcSerialization.deserialize(data, MiniRpcResponse.class);
                if (response != null) {
                    MiniRpcProtocol<MiniRpcResponse> protocol = new MiniRpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
            case HEARTBEAT:
//                todo
                break;
        }
    }
}
