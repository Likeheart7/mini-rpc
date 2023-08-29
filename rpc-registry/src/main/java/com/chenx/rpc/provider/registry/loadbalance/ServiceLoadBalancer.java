package com.chenx.rpc.provider.registry.loadbalance;

import java.util.List;

/**
 * 通用负载均衡接口，所有负载均衡实现都实现该接口
 * @author chenx
 * @create 2023-08-29 10:38
 */
public interface ServiceLoadBalancer<T> {
    /**
     * 选择多个服务实例中的一个
     * @param servers   服务实例集合
     * @param hashCode  哈希码
     * @return  被选中的服务实例
     */
    T select(List<T> servers, int hashCode);
}
