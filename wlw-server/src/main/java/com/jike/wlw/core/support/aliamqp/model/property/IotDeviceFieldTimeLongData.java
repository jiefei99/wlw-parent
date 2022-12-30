package com.jike.wlw.core.support.aliamqp.model.property;

import lombok.Data;

import java.util.Date;

@Data
public class IotDeviceFieldTimeLongData {
    private Date beginDeviceTime;
    private Date endDeviceTime;
    private String beginMessageId;
    private String endMessageId;
    private Long betweenTime;
    private Object beingValue;
    private Object endValue;
    private String key;
    private String deviceName;
    private String productKey;

    private Date createTime;

}
