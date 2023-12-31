package com.chenx.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenx
 * @create 2023-08-23 16:09
 */
@Data
public class MiniRpcResponse implements Serializable {
    private Object data;    //响应结果
    private String message; //错误信息
}
