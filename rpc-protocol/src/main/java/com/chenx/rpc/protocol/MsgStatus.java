package com.chenx.rpc.protocol;

import lombok.Getter;

/**
 * @author chenx
 * @create 2023-08-28 10:17
 */
public enum MsgStatus {
    SUCCESS(0),
    FAIL(1);

    @Getter
    private final int code;
    private MsgStatus(int code) {
        this.code = code;
    }
}
