package com.jike.wlw.core.support.aliamqp.service.property;

import com.jike.wlw.core.support.aliamqp.model.property.IotDevicePropertyData;

public interface IotPropertyHandle {
     Object process(IotDevicePropertyData iotDevicePropertyData);

}
