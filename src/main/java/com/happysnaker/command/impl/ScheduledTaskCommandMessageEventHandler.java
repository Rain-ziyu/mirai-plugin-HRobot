package com.happysnaker.command.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happysnaker.api.JobTaskTriggerApi;
import com.happysnaker.config.RobotConfig;
import com.happysnaker.entity.ContentMessage;
import com.happysnaker.entity.JobTriggerResultDTO;
import com.happysnaker.entity.PageListResultDTO;
import com.happysnaker.exception.CanNotParseCommandException;
import com.happysnaker.exception.InsufficientPermissionsException;
import com.happysnaker.handler.handler;
import com.happysnaker.permission.Permission;
import com.happysnaker.utils.RobotUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author User
 * 用于触发qq发送定时任务，并回调   使用请求xxl调度中心形式
 * 注册消息处理
 */

@handler(priority = 1024)
public class ScheduledTaskCommandMessageEventHandler extends DefaultCommandMessageEventHandlerManager {
    public static final String ADD_PERIOD_TASK = "设置定时任务";
    public static final String START_TASK = "开启任务";
    public static final String STOP_TASK = "关闭任务";
    public static final String TASK_LIST = "任务列表";

    public ScheduledTaskCommandMessageEventHandler() {
        registerKeywords(ADD_PERIOD_TASK);
        registerKeywords(START_TASK);
        registerKeywords(STOP_TASK);
        registerKeywords(TASK_LIST);
    }

    @Override
    public List<MessageChain> parseCommand(MessageEvent event) throws CanNotParseCommandException, InsufficientPermissionsException {
        // 权限判断
        if (Permission.hasAdmin(RobotUtil.getSenderId(event))) {

            String content = getPlantContent(event);
            if (content.startsWith(RobotConfig.commandPrefix + ADD_PERIOD_TASK)) {
                return registerTask(event);
            } else if (content.startsWith(RobotConfig.commandPrefix + START_TASK)) {
                return startTask(event);
            } else if (content.startsWith(RobotConfig.commandPrefix + STOP_TASK)) {
                return stopTask(event);
            } else if (content.startsWith(RobotConfig.commandPrefix + TASK_LIST)) {
                return taskList();
            } else {
                throw new CanNotParseCommandException();
            }
        } else {
            throw new InsufficientPermissionsException("权限不足");
        }
    }

    public List<MessageChain> taskList() {
        PageListResultDTO scheduledTasks = null;
        try {
            scheduledTasks = JobTaskTriggerApi.getScheduledTasks();
        } catch (IOException e) {
            logger.info("获取定时任务列表异常", e);
            return buildMessageChainAsList("获取定时任务列表异常! ");
        }
        List data = scheduledTasks.getData();
        return buildMessageChainAsList("任务列表如下", String.valueOf(returnListBuilder(data)));
    }

    private StringBuffer returnListBuilder(List data) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("任务列表\n");
        for (Object datum : data) {
            JSONObject item = (JSONObject) datum;

            try {
//                              任务id                  任务描述         任务创建者                     任务cron表达式           任务发送内容
                stringBuffer.append(item.get("id") + "|" + item.get("jobDesc") + "|" + item.get("author") + "|" + item.get("scheduleConf") + "|" + ((JSONObject) ((JSONArray) JSON.parseObject((String) item.get("executorParam")).get("messageChain")).get(0)).get("text") + "|" + JSON.parseObject((String) item.get("executorParam")).get("target") + "\n");
            } catch (Exception e) {
                stringBuffer.append(item.get("id") + "|" + item.get("jobDesc") + "|" + item.get("author") + "|" + item.get("scheduleConf") + "|" + item.get("executorParam") + "\n");
            }
        }
        return stringBuffer;
    }

    private List<MessageChain> startTask(MessageEvent event) throws CanNotParseCommandException {
        // 获取命令后的文本字符串
        String content = getPlantContent(event).replace(RobotConfig.commandPrefix + START_TASK, "").trim();
        ContentMessage contentMessage = new ContentMessage(RobotUtil.getSenderId(event), content);
        try {
            JobTriggerResultDTO jobTriggerResultDTO = JobTaskTriggerApi.startScheduledTasks(contentMessage);
            return buildMessageChainAsList("任务开启成功！", JSON.toJSONString(jobTriggerResultDTO));
        } catch (IOException e) {
            logger.info("开启定时任务异常", e);
            return buildMessageChainAsList("开启定时任务异常! ");
        } catch (Exception e) {
            throw new CanNotParseCommandException(e);
        }
    }

    private List<MessageChain> stopTask(MessageEvent event) throws CanNotParseCommandException {
        // 获取命令后的文本字符串
        String content = getPlantContent(event).replace(RobotConfig.commandPrefix + STOP_TASK, "").trim();
        ContentMessage contentMessage = new ContentMessage(RobotUtil.getSenderId(event), content);
        try {
            JobTriggerResultDTO jobTriggerResultDTO = JobTaskTriggerApi.stopScheduledTasks(contentMessage);
            return buildMessageChainAsList("任务关闭成功！", JSON.toJSONString(jobTriggerResultDTO));
        } catch (IOException e) {
            logger.info("开启定时任务异常", e);
            return buildMessageChainAsList("关闭定时任务异常! ");
        } catch (Exception e) {
            throw new CanNotParseCommandException(e);
        }
    }

    @NotNull
    private List<MessageChain> registerTask(MessageEvent event) throws CanNotParseCommandException {
        // 获取命令后的文本字符串
        String content = getPlantContent(event).replace(RobotConfig.commandPrefix + ADD_PERIOD_TASK, "").trim();
//         TODO 发送xxlRESTAPI请求
        ContentMessage contentMessage = new ContentMessage(RobotUtil.getSenderId(event), content);

        try {
            JobTriggerResultDTO jobTriggerResultDTO = JobTaskTriggerApi.sendScheduledTasks(contentMessage);
            return buildMessageChainAsList("任务提交成功！", JSON.toJSONString(jobTriggerResultDTO));
        } catch (IOException e) {
            logger.info("注册定时任务异常", e);
            return buildMessageChainAsList("注册定时任务异常！");
        } catch (Exception e) {
            throw new CanNotParseCommandException(e);
        }
    }
}
