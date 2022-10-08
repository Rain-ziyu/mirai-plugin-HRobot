package com.happysnaker.entity;

import lombok.Data;

@Data
public class JobPageListDTO {
    private String jobGroup = "2";
    private String triggerStatus = "-1";
    private String jobDesc = "";
    private String executorHandler = "";
    private String author = "";
    private String start = "0";
    private Integer length = Integer.MAX_VALUE;
}
