package com.jike.wlw.service.physicalmodel;

import java.io.Serializable;

public enum ThingModelJsonType implements Serializable {
    properties("属性"),
    services("服务"),
    events("事件");

    private String caption;

    private ThingModelJsonType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
