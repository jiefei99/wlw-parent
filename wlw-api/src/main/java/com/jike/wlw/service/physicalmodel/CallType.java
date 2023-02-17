package com.jike.wlw.service.physicalmodel;

import java.io.Serializable;

public enum CallType implements Serializable {
    ASYNC("异步"),
    SYNC("同步");


    private String caption;

    private CallType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
