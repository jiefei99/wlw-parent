package com.jike.wlw.service.product.info;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: PublishStateType
 * @Author RS
 * @Date: 2023/1/30 9:24
 * @Version 1.0
 */
public enum PublishStateType {
    DEVELOPMENT_STATUS("开发中"),
    RELEASE_STATUS("产品已发布");

    private String caption;

    private PublishStateType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public final static Map<String,PublishStateType> map = new HashMap();
    static {
        map.put("DEVELOPMENT_STATUS", PublishStateType.DEVELOPMENT_STATUS);
        map.put("RELEASE_STATUS", PublishStateType.RELEASE_STATUS);
    }
}


