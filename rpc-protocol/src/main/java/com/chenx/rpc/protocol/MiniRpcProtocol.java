package com.chenx.rpc.protocol;

import lombok.Data;

/**
 * @author chenx
 * @create 2023-08-23 16:00
 */
@Data
public class MiniRpcProtocol<T> {
    private MsgHeader header;
//    T 是请求类型或者相应类型
    private T body;
}
