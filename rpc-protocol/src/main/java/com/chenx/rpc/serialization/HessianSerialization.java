package com.chenx.rpc.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author chenx
 * @create 2023-08-23 16:22
 */
public class HessianSerialization implements MiniRpcSerialization{
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if(obj == null)
            throw new NullPointerException();
        byte[] results;
        HessianSerializerOutput hessianOutput;
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            results = os.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        if(data == null) {
            throw new NullPointerException();
        }
        T result;
        try (ByteArrayInputStream os = new ByteArrayInputStream(data)){
            HessianSerializerInput hessianInput = new HessianSerializerInput(os);
            result = (T) hessianInput.readObject(clz);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return result;
    }
}
