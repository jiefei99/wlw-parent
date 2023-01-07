package com.jike.wlw.core.physicalmodel.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: PublishThingModelRq
 * @Author RS
 * @Date: 2023/1/7 17:19
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("发布物模型请求参数")
public class PublishThingModelRq implements Serializable {
    private static final long serialVersionUID = 7461101024272538946L;

    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("查看的物模型版本号")
    private String modelVersion;
    @ApiModelProperty("资源组Id")
    private String resourceGroupId;
    @ApiModelProperty("物模型版本的描述")
    private String description;

}


