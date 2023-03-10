package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUpgradeModuleDeleteRq
 * @Author RS
 * @Date: 2023/3/9 15:29
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("OTA升级模块删除参数")
public class OTAUpgradeModuleDeleteRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698852554276354145L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("模块名称")
    private String moduleName;
}


