package com.jike.wlw.core.physicalmodel.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ThingTemplateRq
 * @Author RS
 * @Date: 2023/1/7 17:12
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("物模型请求参数")
public class ThingTemplateRq implements Serializable {
    private static final long serialVersionUID = 7461101024272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("查询的品类的标识符")
    private String categoryKey;
    @ApiModelProperty("资源组Id")
    private String resourceGroupId;
}


