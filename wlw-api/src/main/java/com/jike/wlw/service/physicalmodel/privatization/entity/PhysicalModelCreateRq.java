package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelCreate
 * @Author RS
 * @Date: 2023/2/20 10:44
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelCreateRq extends Entity {
    private static final long serialVersionUID = 130322029721547602L;

    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("物模型的自定义模块名称")
    private String name;
    @ApiModelProperty("描述")
    private String description;
}


