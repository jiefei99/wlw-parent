package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: PhysicalModelCreateRq
 * @Author RS
 * @Date: 2023/2/15 17:07
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class PhysicalModelCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354115L;
    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("功能定义详情")
    private String thingModelJson;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("物模型的自定义模块名称")
    private String functionBlockName;
    @ApiModelProperty("描述")
    private String details;
}


