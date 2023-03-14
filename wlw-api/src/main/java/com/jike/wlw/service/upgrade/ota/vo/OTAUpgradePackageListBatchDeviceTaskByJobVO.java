package com.jike.wlw.service.upgrade.ota.vo;

import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageListBatchDeviceTaskByJobVO
 * @Author RS
 * @Date: 2023/3/14 12:46
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("OTA设备升级作业")
public class OTAUpgradePackageListBatchDeviceTaskByJobVO extends OTAUpgradePackageListDeviceTaskByJobVO{
    @ApiModelProperty("总数")
    private int total;
    @ApiModelProperty("待确认")
    private int confirmTotal;
    @ApiModelProperty("待推送")
    private int queuedTotal;
    @ApiModelProperty("已推送")
    private int notifiedTotal;
    @ApiModelProperty("升级中")
    private int upgradingTotal;
    @ApiModelProperty("升级成功")
    private int successTotal;
    @ApiModelProperty("升级失败")
    private int failedTotal;
    @ApiModelProperty("已取消")
    private int canceledTotal;
}


