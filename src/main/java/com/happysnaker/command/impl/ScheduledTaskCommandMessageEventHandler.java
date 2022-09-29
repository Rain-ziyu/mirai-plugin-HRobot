package com.happysnaker.command.impl;

import com.happysnaker.handler.handler;

/**
 * @author User
 * 用于触发qq发送定时任务，并回调   使用请求xxl调度中心形式
 * 注册消息处理
 */

@handler(priority = 1024)
public class ScheduledTaskCommandMessageEventHandler extends DefaultCommandMessageEventHandlerManager {
    public static final String ADD_PERIOD_TASK = "设置定时任务";

    public ScheduledTaskCommandMessageEventHandler() {
        registerKeywords(ADD_PERIOD_TASK);
    }
}
