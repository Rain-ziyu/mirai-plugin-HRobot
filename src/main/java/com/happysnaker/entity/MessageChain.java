package com.happysnaker.entity;

import lombok.Data;

@Data
public class MessageChain {
    private String type;

    public MessageChain() {
    }

    public MessageChain(String type) {
        this.type = type;
    }
}
