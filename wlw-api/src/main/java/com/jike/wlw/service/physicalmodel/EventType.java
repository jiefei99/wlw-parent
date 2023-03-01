package com.jike.wlw.service.physicalmodel;

import java.io.Serializable;

public enum EventType implements Serializable {
    INFO("信息"),
    ALERT("告警"),
    ERROR("故障");

    private String caption;

    private EventType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
