package com.happysnaker.entity;


import lombok.Data;

import java.util.List;

/**
 * @author User
 */
@Data
public class SendMessageParam {
    public List<MessageChain> messageChain;
    private String sessionKey;
    private String target;
}
