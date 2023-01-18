package com.jike.wlw.service.product.topic;

import java.io.Serializable;

public enum Operation implements Serializable {
    SUB(0,"sub"), PUB(1,"pub"), ALL(2,"all");

    private String caption;
    private int code;


    private Operation(int code,String caption) {
        this.caption = caption;
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public String getCaption() {
        return caption;
    }
}
