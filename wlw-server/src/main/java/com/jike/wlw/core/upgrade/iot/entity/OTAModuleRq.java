package com.jike.wlw.core.upgrade.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAModule
 * @Author RS
 * @Date: 2023/1/9 14:49
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA模块列表请求参数")
public class OTAModuleRq implements Serializable {
    private static final long serialVersionUID = 7452222224272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("产品的ProductKey")
    private String productKey;
    @ApiModelProperty("OTA模块名称")
    private String moduleName;
    @ApiModelProperty("新模块别名")
    private String aliasName;
    @ApiModelProperty("新的模块描述信息，支持最多100个字符")
    private String desc;
}


