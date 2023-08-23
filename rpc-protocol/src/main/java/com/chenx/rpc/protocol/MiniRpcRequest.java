package com.chenx.rpc.protocol;

import java.io.Serializable;

/**
 * @author chenx
 * @create 2023-08-23 16:06
 */
public class MiniRpcRequest implements Serializable {
    private String serviceVersion;      //服务版本
    private String className;           //服务接口名
    private String methodName;          // 方法名
    private Object[] params;            //方法参数列表
    private Class<?> parameterTypes;    //方法参数类型列表
}
