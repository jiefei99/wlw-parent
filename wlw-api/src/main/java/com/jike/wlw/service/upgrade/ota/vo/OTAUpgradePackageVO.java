package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
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
    @ApiModelProperty("productName")
    private String productName;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("升级包签名")
    private String firmwareSign;
    @ApiModelProperty("升级包版本号")
    private String destVersion;
    @ApiModelProperty("签名算法")
    private String signMethod;
    @ApiModelProperty("升级包状态")
    private Integer status;
    @ApiModelProperty("验证进度")
    private Integer verifyProgress;
    @ApiModelProperty("升级包描述")
    private String firmwareDesc;
    @ApiModelProperty("推送给设备的自定义信息")
    private String udi;
}


