package com.chenx.rpc.serialization;

/**
 * 使用工厂模式实现序列化方式的切换
 * @author chenx
 * @create 2023-08-23 17:13
 */
public class SerializationFactory {
    public static MiniRpcSerialization getRpcSerialization(byte serializationType) {
        SerializationTypeEnum typeEnum = SerializationTypeEnum.findByType(serializationType);
        switch (typeEnum) {
            case JSON:
                return new JsonSerialization();
            case HESSIAN:
                return new HessianSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal, " + serializationType);
        }
    }
}
