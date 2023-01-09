package com.jike.wlw.core.upgrade.iot.entity;

import com.aliyun.iot20180120.models.CreateOTADynamicUpgradeJobRequest;
import com.aliyun.iot20180120.models.CreateOTAStaticUpgradeJobRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTADynamicUpgradeJobRq
 * @Author RS
 * @Date: 2023/1/9 16:25
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("动态升级批次请求参数")
public class OTADynamicUpgradeJobRq implements Serializable {
    private static final long serialVersionUID = 7453333224272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("升级包Id")
    private String firmwareId;
    @ApiModelProperty("产品ProductKey")
    private String productKey;
    @ApiModelProperty("升级范围")
    private String targetSelection;
    @ApiModelProperty("OTA升级时间")
    private Long scheduleTime;
    @ApiModelProperty("自动重试时间间隔")
    private Integer retryInterval;
    @ApiModelProperty("是否覆盖之前的升级任务")
    private Integer overwriteMode=1;
    @ApiModelProperty("自动重试次数")
    private Integer retryCount;
    @ApiModelProperty("升级超时时间")
    private Integer timeoutInMinutes;
    @ApiModelProperty("最大推送设备数")
    private Integer maximumPerMinute;
    @ApiModelProperty("灰度比例")
    private String grayPercent;
    @ApiModelProperty("下载协议")
    private String downloadProtocol;
    @ApiModelProperty("分组Id")
    private String groupId;
    @ApiModelProperty("分组类型")
    private String groupType;
    @ApiModelProperty("定向升级设备列表文件Url")
    private String dnListFileUrl;
    @ApiModelProperty("升级模式")
    private Integer dynamicMode=1;
    @ApiModelProperty("是否支持多模块同时升级")
    private boolean multiModuleMode=false;
    @ApiModelProperty("是否主动向设备推送升级任务")
    private boolean needPush=true;
    @ApiModelProperty("是否自主控制设备OTA升级")
    private boolean needConfirm=false;
    @ApiModelProperty("待升级版本号列表")
    private List<String> srcVersion;
    @ApiModelProperty("定向升级设备名称列表")
    private List<String> targetDeviceName;;
    @ApiModelProperty("批次标签")
    private List<CreateOTADynamicUpgradeJobRequest.CreateOTADynamicUpgradeJobRequestTag> tag;
}


