package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUpgradeModuleCreateRq
 * @Author RS
 * @Date: 2023/3/9 15:51
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("OTA升级模块创建参数")
public class OTAUpgradeModuleCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7298852554276354145L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("模块名称")
    private String moduleName;
    @ApiModelProperty("模块别名")
    private String aliasName;
    @ApiModelProperty("描述")
    private String details;
}


