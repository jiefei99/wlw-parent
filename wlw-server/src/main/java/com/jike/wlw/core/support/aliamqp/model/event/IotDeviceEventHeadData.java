package com.jike.wlw.core.support.aliamqp.model.event;

import lombok.Data;

@Data
public class IotDeviceEventHeadData {
    private String deviceType;
    private String iotId;
    private String requestId;
//    private String deviceNo;
    private String deviceName;
    private String messageId;
    private String topic;
    private String eventType;
}
