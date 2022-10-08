package com.happysnaker.factory;

import com.alibaba.fastjson.JSON;
import com.happysnaker.entity.*;
import lombok.extern.slf4j.Slf4j;
import nlp2cron.CrondUtil;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author User
 * 使用工厂类来生成需要的执行器相关对象
 */
@Slf4j
public class TriggerFactory {

    public static Integer triggerJobGroup = 2;
    public static String triggerJobDesc = "来自Robot创建的任务";
    public static String alarmEmail = "1874300301@qq.com";
    //默认使用CRON进行定时
    public static String scheduleType = "CRON";
    public static String glueType = "BEAN";
    public static String executorParam = "MSG";
    public static String targetPeople = "SENDTO";
    public static String sessionKey = "24681379";
    /**
     * 默认使用处理器的名称
     */
    public static String executorHandler = "qqReminderJob";
    //用于计数
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static TriggerInfoDTO createTriggerInfo(ContentMessage contentMessage) {
        TriggerInfoDTO triggerInfoDTO = new TriggerInfoDTO();
        triggerInfoDTO.setAuthor(contentMessage.getSendId());
        triggerInfoDTO.setJobGroup(2);
        triggerInfoDTO.setJobDesc(triggerJobDesc + atomicInteger.incrementAndGet());
        triggerInfoDTO.setAlarmEmail(alarmEmail);
        triggerInfoDTO.setScheduleType(scheduleType);
        String cron = extractEffectiveInformation(contentMessage.getMessageInfo(), scheduleType);
        triggerInfoDTO.setScheduleConf(cron);
        triggerInfoDTO.setExecutorHandler(executorHandler);
        triggerInfoDTO.setGlueType(glueType);
        triggerInfoDTO.setExecutorParam(extractEffectiveInformation(contentMessage.getMessageInfo(), executorParam));
        return triggerInfoDTO;
    }

    private static String extractEffectiveInformation(String messageContent, String target) {
        if (target.equals(scheduleType)) {
            String cron = null;
            try {
                cron = CrondUtil.toCron(messageContent);
            } catch (Exception e) {
                log.info("该中文输入不匹配cron");
            }
            if (cron != null) {
                return cron;
            } else {
//               如果无法自然语言识别则尝试寻找cron表达式
                String[] split = messageContent.split("\\|");
                String targetString = findTargetString(split, scheduleType);
                String replace = targetString.replace(scheduleType + " ", "");
                cron = replace;
                return cron;
            }
        }
//       寻找发送的消息体
        else if (target.equals(executorParam)) {
            String[] split = messageContent.split("\\|");
//           找到发送内容
            String targetString = findTargetString(split, executorParam);
            String msg = targetString.replace(executorParam + " ", "");
//           寻找发送目标
            String targetString1 = findTargetString(split, targetPeople);
            String targetQQ = targetString1.replace(targetPeople + " ", "");
            return getMessageJSON(targetQQ, msg);
        }
        return "";
    }

    private static String getMessageJSON(String targetQQ, String msg) {
        SendMessageParam sendMessageParam = new SendMessageParam();
        sendMessageParam.setSessionKey(sessionKey);
        sendMessageParam.setTarget(targetQQ);
        PlainMessageChain messageChain = new PlainMessageChain();
        messageChain.setText(msg);
        ArrayList<MessageChain> arrayList = new ArrayList<>();
        arrayList.add(messageChain);
        sendMessageParam.setMessageChain(arrayList);
        return JSON.toJSONString(sendMessageParam);
    }

    /**
     * 方法<code>findTargetString</code>作用为：
     * 如果找到则正常返回，未找到返回null
     *
     * @return java.lang.String
     * @throws
     * @author User
     * No such property: code for class: Script1
     */
    private static String findTargetString(String[] strings, String target) {
        for (String string : strings) {
            if (string.indexOf(target) != -1) {
                return string;
            }
        }
        return null;
    }
}
