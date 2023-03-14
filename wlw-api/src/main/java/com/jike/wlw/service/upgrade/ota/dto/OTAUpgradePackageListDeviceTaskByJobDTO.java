package com.jike.wlw.service.upgrade.ota.dto;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @title: OTAUpgradePackageListDeviceTaskByJobDTO
 * @Author RS
 * @Date: 2023/3/14 11:36
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("OTA设备升级作业")
public class OTAUpgradePackageListDeviceTaskByJobDTO extends StandardEntity {
    private static final long serialVersionUID = 6355633829103467737L;


    @ApiModelProperty("升级的目标OTA升级包版本")
    public String destVersion;
    @ApiModelProperty("设备名称")
    public String deviceName;
    @ApiModelProperty("升级包ID")
    public String firmwareId;
    @ApiModelProperty("设备ID")
    public String iotId;
    @ApiModelProperty("升级批次Id")
    public String jobId;
    @ApiModelProperty("产品productKey")
    public String productKey;
    @ApiModelProperty("产品名称")
    public String productName;
    @ApiModelProperty("当前的升级进度")
    public String progress;
    @ApiModelProperty("设备的原固件版本")
    public String srcVersion;
    @ApiModelProperty("升级作业描述信息")
    public String taskDesc;
    @ApiModelProperty("设备升级作业ID")
    public String taskId;
    @ApiModelProperty("设备升级状态")
    public OTAUpgradePackageTaskStatusType taskStatus;
    @ApiModelProperty("设备升级超时时间")
    public String timeout;
}


