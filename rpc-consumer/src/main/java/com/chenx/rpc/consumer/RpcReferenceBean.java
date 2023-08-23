package com.chenx.rpc.consumer;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author chenx
 * @create 2023-08-23 14:36
 */
public class RpcReferenceBean implements FactoryBean<Object> {
    private Class<?> interfaceClass;

    private String serviceVersion;

    private String registryType;

    private String registryAddr;

    private Object object;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public void init() throws Exception {
//        TODO：生成动态代理对象并赋值给object
    }

//    下面的方法都是setter
    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
