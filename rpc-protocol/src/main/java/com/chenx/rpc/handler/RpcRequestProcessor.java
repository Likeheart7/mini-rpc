package com.chenx.rpc.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenx
 * @create 2023-08-25 9:42
 */
public class RpcRequestProcessor {
    private static ThreadPoolExecutor threadPoolExecutor;

    public static void submitRequest(Runnable task) {
//        第一次进来的时候先给线程池初始化
        if(threadPoolExecutor == null) {
            synchronized (RpcRequestProcessor.class) {
                if(threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
