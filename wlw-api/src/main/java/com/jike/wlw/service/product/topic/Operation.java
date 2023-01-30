package com.jike.wlw.service.product.topic;

import java.io.Serializable;

public enum Operation implements Serializable {
    SUB("订阅"), PUB("发布"), ALL("发布和订阅");

    private String caption;


    private Operation(String caption) {
        this.caption = caption;
    }


    public String getCaption() {
        return caption;
    }
}
