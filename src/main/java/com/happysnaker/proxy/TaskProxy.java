package com.happysnaker.proxy;


import cn.hutool.core.lang.UUID;
import lombok.Data;

/**
 * @author User
 */
@Data
public abstract class TaskProxy implements Runnable {
    private String taskId = UUID.randomUUID().toString();
}
