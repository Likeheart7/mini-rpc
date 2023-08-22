package com.chenx.rpc.provider.registry;

import com.chenx.rpc.common.ServiceMeta;

import java.io.IOException;

/**
 * @author chenx
 * @create 2023-08-22 16:42
 */
public interface RegistryService {
    /**
     * 根据服务数据注册服务
     * @param serviceMeta 服务元数据
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;
    void unRegister(ServiceMeta serviceMeta) throws Exception;
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;
    void destroy() throws IOException;
}
