package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageListDeviceTaskByJobVO
 * @Author RS
 * @Date: 2023/3/14 11:36
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA设备升级作业")
public class OTAUpgradePackageListDeviceTaskByJobVO extends StandardEntity {
    private static final long serialVersionUID = 6355633829103467737L;

    @ApiModelProperty("升级批次Id")
    private String jobId;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("产品productKey")
    private String productKey;
    @ApiModelProperty("当前版本号")
    private String srcVersion;
    @ApiModelProperty("taskId")
    private String taskId;
    @ApiModelProperty("状态")
    private OTAUpgradePackageTaskStatusType taskStatus;
}


