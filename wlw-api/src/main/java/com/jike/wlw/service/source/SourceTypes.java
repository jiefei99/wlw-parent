package com.jike.wlw.service.source;

public enum SourceTypes {

    ALIYUN("阿里云"), MQTT("mqtt"), INFLUXDB("influxdb");

    private String caption;

    private SourceTypes(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}