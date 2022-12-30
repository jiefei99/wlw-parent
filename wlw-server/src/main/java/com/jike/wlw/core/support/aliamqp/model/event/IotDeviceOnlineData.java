package com.jike.wlw.core.support.aliamqp.model.event;

import lombok.Data;

import java.util.Date;

@Data
public class IotDeviceOnlineData {
    private Date lastTime;
    private String clientIp;
    private Date time;
    private String productKey;
    private String deviceName;
    private String status;
}
