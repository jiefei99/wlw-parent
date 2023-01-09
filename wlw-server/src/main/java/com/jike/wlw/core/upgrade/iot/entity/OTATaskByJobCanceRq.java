package com.jike.wlw.core.upgrade.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTATaskByJobCanceRq
 * @Author RS
 * @Date: 2023/1/9 17:31
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("取消批次升级job请求参数")
public class OTATaskByJobCanceRq implements Serializable {
    private static final long serialVersionUID = 7453333334272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("升级包Id")
    private String firmwareId;
    @ApiModelProperty("升级批次Id")
    private String jobId;
    @ApiModelProperty("产品ProductKey")
    private String productKey;
    @ApiModelProperty("取消定时升级批次下的设备升级作业")
    private boolean cancelScheduledTask=false;
    @ApiModelProperty("取消批次下所有状态为待推送的设备升级作业")
    private boolean cancelQueuedTask=false;
    @ApiModelProperty("取消批次下所有状态为升级中的设备升级作业")
    private boolean cancelInProgressTask=false;
    @ApiModelProperty("取消批次下所有状态为已推送的设备升级作业")
    private boolean cancelNotifiedTask=false;
    @ApiModelProperty("取消批次下所有状态为待确认的设备升级作业")
    private boolean cancelUnconfirmedTask;
    @ApiModelProperty("设备名称")
    private List<String> deviceName;;
}


