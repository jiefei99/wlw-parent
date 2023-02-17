package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: PhysicalModelFunctionCreateRq
 * @Author RS
 * @Date: 2023/2/17 14:18
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelFunctionCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354115L;

    @ApiModelProperty("物模型Id")
    private String modelDeviceId;
    @ApiModelProperty("json")
    private String thingModelJson;
}


