package com.jike.wlw.service.physicalmodel;

import java.io.Serializable;

public enum EventType implements Serializable {
    INFO("信息"),
    WARNING("告警"),
    FAULT("故障");

    private String caption;

    private EventType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
