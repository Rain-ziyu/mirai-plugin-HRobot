package com.happysnaker.entity;

import lombok.Data;

@Data
public class JobTriggerDTO {
    /**
     * 任务ID
     */
    private String jobId;
    /**
     * 任务标识
     */
    private String executorHandler;
    /**
     * 任务参数
     */
    private String executorParams;
    /**
     * 任务阻塞策略，可选值参考 com.xxl.job.core.enums.ExecutorBlockStrategyEnum
     */
    private String executorBlockStrategy;
    /**
     * 任务超时时间，单位秒，大于零时生效
     */
    private String executorTimeout;
    /**
     * 本次调度日志ID
     */
    private String logId;
    /**
     * 本次调度日志时间
     */
    private String logDateTime;
    /**
     * 任务模式，可选值参考 com.xxl.job.core.glue.GlueTypeEnum
     */
    private String glueType;
    /**
     * GLUE脚本代码
     */
    private String glueSource;
    /**
     * GLUE脚本更新时间，用于判定脚本是否变更以及是否需要刷新
     */
    private String glueUpdatetime;
    /**
     * 分片参数：当前分片
     */
    private String broadcastIndex;
    /**
     * 分片参数：总分片
     */
    private String broadcastTotal;
}
