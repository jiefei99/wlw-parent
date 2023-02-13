package com.jike.wlw.service.source;

/**
 * @author wza
 * @create 2023/2/13
 */
public enum SourceStatus {
    CONNECTED("已连接"), DISCONNECT("未连接"), CONNECTFAILED("连接失败");

    private String caption;

    private SourceStatus(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
