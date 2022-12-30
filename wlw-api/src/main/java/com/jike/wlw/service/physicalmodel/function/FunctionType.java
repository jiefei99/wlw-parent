package com.jike.wlw.service.physicalmodel.function;

public enum FunctionType {

    PROPERTY("属性"), EVENT("事件"), SERVE("服务");

    private String caption;

    private FunctionType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
