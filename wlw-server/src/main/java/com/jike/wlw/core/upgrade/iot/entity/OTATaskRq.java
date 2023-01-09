package com.jike.wlw.core.upgrade.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTATaskRq
 * @Author RS
 * @Date: 2023/1/9 14:30
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("升级作业请求参数")
public class OTATaskRq implements Serializable {
    private static final long serialVersionUID = 7451222224272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("设备升级作业ID")
    private List<String> taskId;
    @ApiModelProperty("升级状态")
    private String taskStatus;
    @ApiModelProperty("OTA模块名称")
    private String moduleName;
    @ApiModelProperty("所属产品的ProductKey")
    private String productKey;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("IotId")
    private String  iotId;
}


