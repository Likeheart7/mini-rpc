package com.chenx.rpc.provider.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenx
 * @create 2023-08-22 15:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {
//    服务版本
    String serviceVersion() default "1.0";
//    服务类型
    Class<?> serviceInterface() default Object.class;
}
