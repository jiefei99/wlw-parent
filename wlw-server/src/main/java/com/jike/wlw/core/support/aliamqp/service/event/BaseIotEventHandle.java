package com.jike.wlw.core.support.aliamqp.service.event;



import com.jike.wlw.core.support.aliamqp.factory.AliIotFactory;
import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceEventData;

import javax.annotation.PostConstruct;


public  abstract  class BaseIotEventHandle implements IotEventHandle {
    @Override
    public void process(String eventType, IotDeviceEventData eventData) {
        Object eventRecordData =    tranData(eventData);
//        MongodbUtils.insert(getTemplateName(),eventRecordData,eventType);
        processAfter(eventType,eventRecordData);

    }
    @PostConstruct
    public  void  registHandle(){
        AliIotFactory.registHandle(getEventType(), this);
    }

    public abstract String getEventType();
    public Object tranData(IotDeviceEventData eventData){
        return eventData;
    };

    public void processAfter(String eventType, Object eventData){

    }

    public String getTemplateName(){
        return null;
    };
}
