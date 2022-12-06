package com.jike.wlw.service.message;

import io.swagger.annotations.ApiModel;

/**
 * @Author ZhengZhouDong
 * @Date 2020/5/14 17:10
 */
@ApiModel("消息类型")
public enum MessageType {

    /**
     * 订单
     */
    WORKBACK("路线退回"),
    /**
     * 活动公告
     */
    ACTIVITY("活动公告"),
    /**
     * 系统消息
     */
    SYSTEM("系统消息");

    private String caption;

    private MessageType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
