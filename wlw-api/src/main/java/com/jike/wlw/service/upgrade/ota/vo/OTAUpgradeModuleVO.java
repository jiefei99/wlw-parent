package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradeModuleVO
 * @Author RS
 * @Date: 2023/3/9 15:07
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA升级模块")
public class OTAUpgradeModuleVO extends StandardEntity {
    private static final long serialVersionUID = 6355633899103467737L;

    @ApiModelProperty("模块别名")
    private String aliasName;
    @ApiModelProperty("模块描述")
    private String details;
    @ApiModelProperty("产品")
    private String productKey;
    @ApiModelProperty("模块名称")
    private String moduleName;
}


