package com.chenx.rpc.common;

/**
 * @author chenx
 * @create 2023-08-22 17:16
 */
public class RpcServiceHelper {
    public static String buildServiceKey(String serviceName, String ServiceVersion) {
        return String.join("#", serviceName, ServiceVersion);
    }
}
