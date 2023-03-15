package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.base.Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel("OTA升级包验证任务参数")
public class OTAUpgradePackageVerifyJobCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354145L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("升级包ID")
    private String firmwareId;
    @ApiModelProperty("升级包下载协议")
    private String downloadProtocol;
    @ApiModelProperty("自主控制设备OTA升级")
    private Boolean needConfirm = Boolean.FALSE;
    @ApiModelProperty("主动向设备推送升级任务")
    private Boolean needPush = Boolean.TRUE;
    @ApiModelProperty("升级超时时间")
    private Integer timeoutInMinutes;
    @ApiModelProperty("待验证设备集合")
    private List<String> targetDeviceNameIn;
    @ApiModelProperty("批次标签")
    private List<Parameter> tagList;
}
