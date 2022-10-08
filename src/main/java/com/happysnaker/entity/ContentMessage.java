package com.happysnaker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author User
 * 用于将用户文本内容转换为实体
 */
@Data
@AllArgsConstructor
public class ContentMessage {
    private String sendId;
    private String messageInfo;

}
