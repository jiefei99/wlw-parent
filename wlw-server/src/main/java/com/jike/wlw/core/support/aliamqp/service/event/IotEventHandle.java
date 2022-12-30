package com.jike.wlw.core.support.aliamqp.service.event;

import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceEventData;

public interface IotEventHandle {
     void process(String eventType, IotDeviceEventData eventData);
}
