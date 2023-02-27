package com.jike.wlw.service.physicalmodel;

import java.io.Serializable;

public enum DirectionType implements Serializable {
    PARAM_INPUT("输入参数"),
    PARAM_OUTPUT("输出参数");

    private String caption;

    private DirectionType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
