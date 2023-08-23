package com.chenx.rpc.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenx
 * @create 2023-08-22 15:21
 */
@Data
@ConfigurationProperties("rpc")
public class RpcProperties {
//    服务暴露的端口
    private int servicePort;

//    注册中心的地址
    private String registryAddr;

//    注册中心的类型。取值在RegistryType中枚举，包括ZOOKEEPER、EUREKA
    private String registryType;
}
