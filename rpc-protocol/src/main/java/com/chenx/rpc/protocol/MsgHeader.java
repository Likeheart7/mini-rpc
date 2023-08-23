package com.chenx.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenx
 * @create 2023-08-23 16:00
 */
@Data
public class MsgHeader  implements Serializable {
    private short magic;    // 魔数
    private byte version;   // 协议版本号
    private byte serialization; //序列化算法
    private byte msgType;   //报文类型
    private byte status;    //状态 0：成功 !0：失败
    private long requestId;     //消息id
    private int msgLen;     // 数据长度
}
