package com.chenx.rpc.consumer.controller;

import com.chenx.rpc.consumer.annotation.RpcReference;
import com.chenx.rpc.provider.facade.HelloFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenx
 * @create 2023-08-29 16:03
 */
@RestController
public class HelloController {
    @SuppressWarnings({"SpringJavaAutowireFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @RpcReference
    private HelloFacade helloFacade;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello() {
        return helloFacade.hello("mini rpc");
    }

}
