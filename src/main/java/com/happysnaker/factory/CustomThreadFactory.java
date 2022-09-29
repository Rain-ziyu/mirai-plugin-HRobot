package com.happysnaker.factory;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author User
 * 自定义创建线程，并命名
 */
public class CustomThreadFactory implements ThreadFactory {
    public static AtomicInteger threadNO = new AtomicInteger();

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread thread = new Thread(r, "Robot-scheduledExecutorService" + threadNO.incrementAndGet());
        return thread;
    }
}
