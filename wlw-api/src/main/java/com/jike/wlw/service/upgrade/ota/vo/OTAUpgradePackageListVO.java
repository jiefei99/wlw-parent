package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageListVO
 * @Author RS
 * @Date: 2023/3/8 17:03
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA升级包")
public class OTAUpgradePackageListVO extends StandardEntity {
    private static final long serialVersionUID = 6355633899103467737L;

    @ApiModelProperty("升级包版本号")
    private String destVersion;
    @ApiModelProperty("升级包名称")
    private String firmwareName;
    @ApiModelProperty("升级包类型")
    private OTAUpgradePackageType type;
    @ApiModelProperty("状态")
    private OTAUpgradePackageStatusType statusType;
    @ApiModelProperty("所属产品名称")
    private String productName;
    @ApiModelProperty("所属产品productKey")
    private String productKey;
    @ApiModelProperty("升级包id")
    private String firmwareId;
    @ApiModelProperty("模块名称")
    private String moduleName;
    @ApiModelProperty("待升OTA升级包版本号")//整包返回为空！
    private String srcVersion;
}


