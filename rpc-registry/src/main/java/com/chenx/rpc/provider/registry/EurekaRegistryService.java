package com.chenx.rpc.provider.registry;

import com.chenx.rpc.common.ServiceMeta;

import java.io.IOException;

/**
 * @author chenx
 * @create 2023-08-22 17:28
 */
public class EurekaRegistryService implements RegistryService{

    public EurekaRegistryService(String registryAddr) {
//        todo
    }
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws IOException {

    }
}
