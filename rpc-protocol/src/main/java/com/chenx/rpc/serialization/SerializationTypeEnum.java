package com.chenx.rpc.serialization;

import lombok.Getter;

/**
 * @author chenx
 * @create 2023-08-23 17:15
 */
public enum SerializationTypeEnum {
    JSON(0x10),
    HESSIAN(0X20);
    @Getter
    private final int type;

    SerializationTypeEnum(int type) {
        this.type = type;
    }

    public static SerializationTypeEnum findByType(byte serializationType) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if(typeEnum.getType() == serializationType) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }
}
