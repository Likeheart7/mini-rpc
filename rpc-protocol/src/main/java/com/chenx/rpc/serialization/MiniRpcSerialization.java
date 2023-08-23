package com.chenx.rpc.serialization;

import java.io.IOException;

/**
 * @author chenx
 * @create 2023-08-23 16:20
 */
public interface MiniRpcSerialization {
    <T> byte[] serialize(T obj) throws IOException;
    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
