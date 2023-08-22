package com.chenx.rpc.provider.registry;

/**
 * 是个单例模式
 *
 * @author chenx
 * @create 2023-08-22 17:21
 */
public class RegistryFactory {
    private static volatile RegistryService registryService;

    /**
     * 根据服务注册的地址和类型创建一个RegistryService的实例
     *
     * @param registryAddr ip地址
     * @param type         类型（Zookeeper / Eureka）
     * @return RegistryService实例
     * @throws Exception
     */
    public static RegistryService getInstance(String registryAddr, RegistryType type) throws Exception {
        if (registryService == null) {
            synchronized (RegistryFactory.class) {
                if (registryService == null) {
                    switch (type) {
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistryService(registryAddr);
                            break;
                        case EUREKA:
                            registryService = new EurekaRegistryService(registryAddr);
                            break;
                    }
                }
            }
        }
        return registryService;
    }
}
