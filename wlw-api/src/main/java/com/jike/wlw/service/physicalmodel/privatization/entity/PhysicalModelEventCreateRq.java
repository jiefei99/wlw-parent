package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: PhysicalModelEventCreateRq
 * @Author RS
 * @Date: 2023/2/17 10:46
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelEventCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354115L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("物模型Id")
    private String modelDeviceId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("事件类型")
    private String type;
    @ApiModelProperty("事件对应的方法名称")
    private String method;
    @ApiModelProperty("是否是标准功能的必选事件")
    private String required;
    @ApiModelProperty("描述")
    private String details;
}


