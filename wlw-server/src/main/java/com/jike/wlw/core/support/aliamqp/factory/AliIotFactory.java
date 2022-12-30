package com.jike.wlw.core.support.aliamqp.factory;

import com.jike.wlw.core.support.aliamqp.config.AliIotConfig;
import com.jike.wlw.core.support.aliamqp.service.event.IotEventHandle;
import com.jike.wlw.core.support.aliamqp.service.event.IotOnlineEventHandle;
import com.jike.wlw.core.support.aliamqp.service.event.IotOnlineEventService;
import com.jike.wlw.core.support.aliamqp.service.property.IotPropertyHandle;
import com.jike.wlw.core.support.aliamqp.service.property.IotPropertyService;

public class AliIotFactory {
    public static void registHandle(String eventType, IotEventHandle iotEventHandle){
        AliIotConfig.registHandle(eventType,iotEventHandle);
    }
    public static void  registOnlineHandle(IotOnlineEventHandle iotOnlineEventHandle){
        IotOnlineEventService.registHandle(iotOnlineEventHandle);
    }
    public static void  registIotPropertyHandle(String productKey, IotPropertyHandle iotPropertyHandle){
        IotPropertyService.registHandle(productKey,iotPropertyHandle);
    }
}
