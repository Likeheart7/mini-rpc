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
    private int servicePort;

    private String registryAddr;

//    取值在RegistryType中枚举，包括ZOOKEEPER、EUREKA
    private String registryType;
}
