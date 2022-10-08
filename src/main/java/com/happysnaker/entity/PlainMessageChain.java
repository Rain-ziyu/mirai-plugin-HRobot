package com.happysnaker.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlainMessageChain extends MessageChain {
    private String text;

    public PlainMessageChain() {
        super("Plain");
    }
}
