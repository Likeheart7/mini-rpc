package com.chenx.rpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author chenx
 * @create 2023-08-22 14:41
 */
@SpringBootApplication
public class RpcProviderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RpcProviderApplication.class, args);

    }
}
