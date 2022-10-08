package com.happysnaker.entity;

import lombok.Data;

/**
 * @author User
 * 用于创建执行器相关的信息
 */
@Data
public class TriggerInfoDTO {

    private int id;                // 主键ID

    private int jobGroup;        // 执行器主键ID
    private String jobDesc;
    private String author;        // 负责人
    private String alarmEmail;    // 报警邮件
    private String scheduleType;            // 调度类型
    private String scheduleConf;            // 调度配置，值含义取决于调度类型
    private String misfireStrategy = "DO_NOTHING";            // 调度过期策略
    private String executorRouteStrategy = "FIRST";    // 执行器路由策略
    private String executorHandler;            // 执行器，任务Handler名称
    private String executorParam;            // 执行器，任务参数
    private String executorBlockStrategy = "SERIAL_EXECUTION";    // 阻塞处理策略
    private int executorTimeout = 0;            // 任务执行超时时间，单位秒
    private int executorFailRetryCount = 0;        // 失败重试次数
    private String glueType;        // GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
    private String glueSource;        // GLUE源代码
    private String glueRemark = "GLUE代码初始化";        // GLUE备注

    private String childJobId = "";        // 子任务ID，多个逗号分隔
    private int triggerStatus;        // 调度状态：0-停止，1-运行
    private long triggerLastTime;    // 上次调度时间
    private long triggerNextTime;    // 下次调度时间
}
