package com.jike.wlw.core.support.aliamqp.model.event;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class IotDeviceEventData   {
    private IotDeviceEventHeadData headData;
    private JSONObject eventData;


}
