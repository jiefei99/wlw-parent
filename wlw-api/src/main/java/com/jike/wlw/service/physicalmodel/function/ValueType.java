package com.jike.wlw.service.physicalmodel.function;

public enum ValueType {

    INT("整型"), FLOAT("单精度浮点型"), DOUBLE("双精度浮点型"), STRING("字符串"), DATE("时间戳"), BOOL("布尔型");

    private String caption;

    private ValueType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
