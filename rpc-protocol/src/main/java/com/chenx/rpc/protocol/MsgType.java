package com.chenx.rpc.protocol;

import lombok.Getter;

/**
 * @author chenx
 * @create 2023-08-24 10:53
 */
public enum MsgType {
    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);
    @Getter
    int type;

    MsgType(int type) {
        this.type = type;
    }

    public static MsgType findByType(int type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type)
                return msgType;
        }
        return null;
    }
}
