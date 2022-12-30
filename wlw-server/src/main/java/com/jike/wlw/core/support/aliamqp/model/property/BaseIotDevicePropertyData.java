package com.jike.wlw.core.support.aliamqp.model.property;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseIotDevicePropertyData {
    private String messageId;
//    private String deviceNo;
    private String deviceName;
    private String productKey;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
