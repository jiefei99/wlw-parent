package com.jike.wlw.core.support.aliamqp.service.event;

import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceOnlineData;

public interface IotOnlineEventHandle {
     void process(String deviceName,String productKey, IotDeviceOnlineData deviceOnlineData);
}
