package com.jike.wlw.core.support.aliamqp.model.property;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


@Data
public class IotDevicePropertyData extends BaseIotDevicePropertyData{
    private String topic;
    private JSONObject items;

}
