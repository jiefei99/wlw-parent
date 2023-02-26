package com.jike.wlw.service.physicalmodel;

import java.io.Serializable;

public enum DataType implements Serializable {
    INT("整型"),
    FLOAT("单精度浮点型"),
    DOUBLE("双精度浮点型"),
    ENUM("枚举型"),
    TEXT("字符串"),
    DATE("时间型"),
    ARRAY("数组"),
    STRUCT("结构体"),
    BOOL("布尔型");


    private String caption;

    private DataType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
