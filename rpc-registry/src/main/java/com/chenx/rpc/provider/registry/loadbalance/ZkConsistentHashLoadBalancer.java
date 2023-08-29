package com.chenx.rpc.provider.registry.loadbalance;

import com.chenx.rpc.common.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author chenx
 * @create 2023-08-29 10:44
 */
public class ZkConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {

    public static final int VIRTUAL_NODE_SIZE = 10;
    public static final String VIRTUAL_NODE_SPLIT = "#";
    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
//        构造哈希环
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeConsistentHashRing(servers);
        return allocateNode(ring, hashCode);
    }

    private ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
//        给出key相等的实体或距离最近的大一点的，如果没有（即给出的键是最大的），就返回null
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
//            如果没有返回值，返回第一个实例
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * TreeMap会默认根据hashcode从大到小排序
     * @param servers   服务实例们
     * @return  返回哈希环
     */
    private TreeMap<Integer, ServiceInstance<ServiceMeta>> makeConsistentHashRing(List<ServiceInstance<ServiceMeta>> servers) {
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = new TreeMap<>();
        for (ServiceInstance<ServiceMeta> instance : servers) {
//            todo：不太理解这一块虚拟节点的实际作用
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
            }
        }
        return ring;
    }

    /**
     * 根据服务实例的IP地址和端口号生成其对应在哈希环中的key
     * @param instance
     * @return
     */
    private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> instance) {
        ServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getServiceAddr(), String.valueOf(payload.getServicePort()));
    }
}
