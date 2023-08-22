package com.chenx.rpc.common;

import lombok.Data;

/**
 * @author chenx
 * @create 2023-08-22 16:43
 */
@Data
public class ServiceMeta {
    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;
}
