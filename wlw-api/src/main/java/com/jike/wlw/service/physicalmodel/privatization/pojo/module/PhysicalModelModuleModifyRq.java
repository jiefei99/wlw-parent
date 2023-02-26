package com.jike.wlw.service.physicalmodel.privatization.pojo.module;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: PhysicalModelModuleModifyRq
 * @Author RS
 * @Date: 2023/2/24 9:37
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelModuleModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354329L;


    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("模块名称")
    private String name;
    @ApiModelProperty("描述")
    private String details;
}


