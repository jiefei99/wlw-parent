package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageVO
 * @Author RS
 * @Date: 2023/3/13 16:42
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA升级包")
public class OTAUpgradePackageVO  extends StandardEntity {
    private static final long serialVersionUID = 6355633899103337737L;
    @ApiModelProperty("升级包ID")
    private String firmwareId;
    @ApiModelProperty("升级包名称")
    private String firmwareName;
    @ApiModelProperty("升级包签名")
    private String firmwareSign;
    @ApiModelProperty("签名算法")
    private String signMethod;
    @ApiModelProperty("类型")
    private OTAUpgradePackageType type;
    @ApiModelProperty("模块")
    private String moduleName;
    @ApiModelProperty("目标设备升级总数")
    private int targetDeviceUpgradeTotal;
    @ApiModelProperty("目标成功数")
    private int targetSuccessTotal;
    @ApiModelProperty("目标失败书")
    private int targetFailTotal;
    @ApiModelProperty("目标取消数")
    private int targetCancelTotal;
}

