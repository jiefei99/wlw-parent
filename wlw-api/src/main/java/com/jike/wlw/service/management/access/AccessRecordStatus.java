package com.jike.wlw.service.management.access;

import io.swagger.annotations.ApiModel;


@ApiModel("浏览记录状态")
public enum AccessRecordStatus {

    NORMAL("正常"),

    ABNORMAL("异常");

    private String caption;

    private AccessRecordStatus(String caption) {
        this.caption = caption;
    }
}