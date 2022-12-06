package com.jike.wlw.service.support.iconconfig;

public enum AppType {

    WXA("小程序");

    private String caption;

    private AppType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
