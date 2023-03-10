package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTAUpgradePackageCreateRq
 * @Author RS
 * @Date: 2023/3/9 14:36
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("创建OTA升级包请求参数")
public class OTAUpgradePackageCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354145L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("升级包版本")
    private String destVersion;
    @ApiModelProperty("升级包版本")
    private String firmwareName;
    @ApiModelProperty("文件URL")
    private String firmwareUrl;
    @ApiModelProperty("文件签名值")
    private String firmwareSign;
    @ApiModelProperty("OTA升级包签名方法")
    private String signMethod;
    @ApiModelProperty("OTA升级包文件的大小")
    private Integer firmwareSize;
    @ApiModelProperty("升级包描述")
    private String firmwareDesc;
    @ApiModelProperty("升级包类型")
    private Integer type;
    @ApiModelProperty("待升级OTA模块版本号")
    private String srcVersion;
    @ApiModelProperty("OTA模块名称")
    private String moduleName;
    @ApiModelProperty("是否需要验证")
    private boolean needToVerify;
    @ApiModelProperty("推送给设备的自定义信息")
    private String udi;
    @ApiModelProperty("OTA升级包")
    private List<OTAFirmwareMultiFilesCreateRq> multiFiles;
}


