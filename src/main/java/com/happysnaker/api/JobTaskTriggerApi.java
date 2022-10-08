package com.happysnaker.api;

import com.alibaba.fastjson.JSON;
import com.happysnaker.entity.*;
import com.happysnaker.factory.TriggerFactory;
import com.happysnaker.utils.HttpUtil;
import com.happysnaker.utils.IOUtil;
import com.happysnaker.utils.ReflectionUtil;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author User
 * 用于向调度中心注册定时任务
 */
public class JobTaskTriggerApi {
    /**
     * 远程调度中心地址
     */
    public static final String REMOTE_DISPATCHING_CENTER = "http://localhost:9080/xxl-job-admin";

    /**
     * 方法sendScheduledTasks
     * 作用为：向调度中心发送定时任务
     *
     * @param
     * @return com.happysnaker.entity.JobTriggerResultDTO
     * @throws
     * @author User
     */
    public static JobTriggerResultDTO sendScheduledTasks(ContentMessage content) throws IOException {
        TriggerInfoDTO triggerInfo = TriggerFactory.createTriggerInfo(content);
        String remoteToken = getRemoteToken();
        HashMap hashMap = new HashMap();
        hashMap.put("Cookie", remoteToken);
        String[] filedName = ReflectionUtil.getFiledName(triggerInfo);
        StringBuffer params = new StringBuffer();
        for (String s : filedName) {
            params.append("&").append(s).append("=").append(ReflectionUtil.getFieldValueByName(s, triggerInfo));
        }
        String response = "";
        response = IOUtil.sendAndGetResponseStringByForm(new URL(REMOTE_DISPATCHING_CENTER + "/jobinfo/add"), "POST", hashMap, params.substring(1));
        JobTriggerResultDTO jobTriggerResultDTO = JSON.parseObject(response, JobTriggerResultDTO.class);
        return jobTriggerResultDTO;
    }

    public static PageListResultDTO getScheduledTasks() throws IOException {
        JobPageListDTO jobPageListDTO = new JobPageListDTO();
        String remoteToken = getRemoteToken();
        HashMap hashMap = new HashMap();
        hashMap.put("Cookie", remoteToken);
        String[] filedName = ReflectionUtil.getFiledName(jobPageListDTO);
        StringBuffer params = new StringBuffer();
        for (String s : filedName) {
            params.append("&").append(s).append("=").append(ReflectionUtil.getFieldValueByName(s, jobPageListDTO));
        }
        String response = "";
        response = IOUtil.sendAndGetResponseStringByForm(new URL(REMOTE_DISPATCHING_CENTER + "/jobinfo/pageList"), "POST", hashMap, params.substring(1));
        PageListResultDTO pageListResultDTO = JSON.parseObject(response, PageListResultDTO.class);
        return pageListResultDTO;
    }

    public static JobTriggerResultDTO startScheduledTasks(ContentMessage contentMessage) throws IOException {
//        header区域
        String remoteToken = getRemoteToken();
        HashMap hashMap = new HashMap();
        hashMap.put("Cookie", remoteToken);
        StringBuffer params = new StringBuffer();
//        将文本内容直接拼接到表单中
        params.append("id=" + contentMessage.getMessageInfo());
        String response = "";
        response = IOUtil.sendAndGetResponseStringByForm(new URL(REMOTE_DISPATCHING_CENTER + "/jobinfo/start"), "POST", hashMap, String.valueOf(params));
        JobTriggerResultDTO jobTriggerResultDTO = JSON.parseObject(response, JobTriggerResultDTO.class);
        return jobTriggerResultDTO;
    }

    public static JobTriggerResultDTO stopScheduledTasks(ContentMessage contentMessage) throws IOException {
//        header区域
        String remoteToken = getRemoteToken();
        HashMap hashMap = new HashMap();
        hashMap.put("Cookie", remoteToken);
        StringBuffer params = new StringBuffer();
//        将文本内容直接拼接到表单中
        params.append("id=" + contentMessage.getMessageInfo());
        String response = "";
        response = IOUtil.sendAndGetResponseStringByForm(new URL(REMOTE_DISPATCHING_CENTER + "/jobinfo/stop"), "POST", hashMap, String.valueOf(params));
        JobTriggerResultDTO jobTriggerResultDTO = JSON.parseObject(response, JobTriggerResultDTO.class);
        return jobTriggerResultDTO;
    }

    public static String getRemoteToken() {
        HashMap formData = new HashMap();
        formData.put("userName", "admin");
        formData.put("password", "123456");
        formData.put("ifRemember", "false");
        Response response = null;
        try {
            response = HttpUtil.sendRequestByFormData(REMOTE_DISPATCHING_CENTER + "/login", formData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String xxl_job_login_identity = response.header("Set-Cookie");
        return xxl_job_login_identity;
    }
}
