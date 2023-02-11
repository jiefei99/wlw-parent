package com.jike.wlw.service.source;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/2/10
 */
public enum SourceEvns implements Serializable {
    CLOUD("云"),LOCAL("本地");
    private String caption;

    private SourceEvns(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
