package com.jike.wlw.service.physicalmodel.privatization.pojo.module;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: PhysicalModelModule
 * @Author RS
 * @Date: 2023/2/24 10:41
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelModule extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554275354219L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("模块名称")
    private String name;
    @ApiModelProperty("模块表示符")
    private String identifier;
    @ApiModelProperty("描述")
    private String details;
}


