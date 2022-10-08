package com.happysnaker.entity;


import lombok.Getter;
import lombok.Setter;


/**
 * @author User
 */
@Getter
@Setter

public class ImageMessageChain extends MessageChain {
    private String url;

    public ImageMessageChain(String type) {
        super(type);
    }
}
