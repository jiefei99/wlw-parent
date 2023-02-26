package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: PhysicalModelFunctionDelRq
 * @Author RS
 * @Date: 2023/2/23 17:32
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelFunctionDelRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354215L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("模块标识符")
    private String moduleIdentifier;
    @ApiModelProperty("功能标识符")
    private List<String> identifierIn;
    @ApiModelProperty("功能标识符")
    private String identifierEq;
}


