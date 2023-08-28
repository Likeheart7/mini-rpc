package com.chenx.rpc.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author chenx
 * @create 2023-08-28 10:41
 */
@Data
public class MiniRpcFuture<T> {
    private Promise<T> promise;
    private long timeout;

    public MiniRpcFuture(Promise<T> promise, long timeout) {
        this.timeout = timeout;
        this.promise = promise;
    }
}
