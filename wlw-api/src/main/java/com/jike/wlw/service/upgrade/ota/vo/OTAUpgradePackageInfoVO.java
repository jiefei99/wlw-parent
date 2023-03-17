/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月13日 12:24 - rs - 创建。
 */
package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author rs
 * @since 1.0
 */

@Setter
@Getter
@ApiModel("OTA升级包")
public class OTAUpgradePackageInfoVO extends StandardEntity {
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
    @ApiModelProperty("待升级版本号")
    private String srcVersion;
    @ApiModelProperty("签名算法")
    private String signMethod;
    @ApiModelProperty("升级包状态")
    private OTAUpgradePackageStatusType status;
    @ApiModelProperty("验证进度")
    private Integer verifyProgress;
    @ApiModelProperty("升级包描述")
    private String firmwareDesc;
    @ApiModelProperty("推送给设备的自定义信息")
    private String udi;
}


