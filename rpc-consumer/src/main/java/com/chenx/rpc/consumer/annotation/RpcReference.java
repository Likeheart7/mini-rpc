package com.chenx.rpc.consumer.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenx
 * @create 2023-08-23 11:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {
//    服务版本
    String serviceVersion() default "1.0.0";
//    注册中心类型
    String registryType() default "ZOOKEEPER";
//    注册地址
    String registryAddress() default "127.0.0.1:2181";
//    超时时间
    long timeout() default 5000;
}
