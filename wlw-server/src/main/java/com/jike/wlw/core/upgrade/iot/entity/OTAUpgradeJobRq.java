package com.jike.wlw.core.upgrade.iot.entity;

import com.aliyun.iot20180120.models.CreateOTAVerifyJobRequest;
import com.aliyun.iot20180120.models.CreateOTAVerifyJobRequest.CreateOTAVerifyJobRequestTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTAUpgradeJob
 * @Author RS
 * @Date: 2023/1/9 16:08
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("升级包任务请求参数")
public class OTAUpgradeJobRq  implements Serializable {
    private static final long serialVersionUID = 7453322224272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("升级包Id")
    private String firmwareId;
    @ApiModelProperty("产品ProductKey")
    private String productKey;
    @ApiModelProperty("升级包下载协议")
    private String downloadProtocol;
    @ApiModelProperty("升级超时时间")
    private Integer timeoutInMinutes;
    @ApiModelProperty("是否主动向设备推送升级任务")
    private boolean needPush=true;
    @ApiModelProperty("是否自主控制设备OTA升级")
    private boolean needConfirm=false;
    @ApiModelProperty("批次标签")
    private List<CreateOTAVerifyJobRequestTag> tag;
    @ApiModelProperty("待验证设备")
    private List<String> targetDeviceName;
}


