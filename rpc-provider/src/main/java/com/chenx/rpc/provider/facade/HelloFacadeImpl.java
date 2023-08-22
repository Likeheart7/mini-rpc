package com.chenx.rpc.provider.facade;

import com.chenx.rpc.provider.annotation.RpcService;

/**
 * @author chenx
 * @create 2023-08-22 15:13
 */
@RpcService(serviceInterface = HelloFacade.class, serviceVersion = "1.0.0")
public class HelloFacadeImpl implements HelloFacade{

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
