package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUpgradePackageCancelTaskByJobRq
 * @Author RS
 * @Date: 2023/3/15 11:45
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("取消指定批次下的设备升级作业参数")
public class OTAUpgradePackageCancelTaskByJobRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859154273354145L;

    @ApiModelProperty("升级批次ID")
    private String jobId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("取消定时升级批次下的设备升级作业")
    private Boolean cancelScheduledTask = Boolean.FALSE;
    @ApiModelProperty("取消批次下所有状态为待推送（QUEUED）的设备升级作业")
    private Boolean cancelQueuedTask = Boolean.FALSE;
    @ApiModelProperty("取消批次下所有状态为升级中（IN_PROGRESS）的设备升级作业")
    private Boolean cancelInProgressTask = Boolean.FALSE;
    @ApiModelProperty("取消批次下所有状态为已推送（（NOTIFIED）的设备升级作业")
    private Boolean cancelNotifiedTask = Boolean.FALSE;
    @ApiModelProperty("取消批次下所有状态为待确认（CONFIRM）的设备升级作业")
    private Boolean cancelUnconfirmedTask;
}


