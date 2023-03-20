package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.base.Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @title: OTAUpgradePackageStaticUpgradeJobCreateRq
 * @Author RS
 * @Date: 2023/3/15 11:58
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("创建静态升级批次参数")
public class OTAUpgradePackageStaticUpgradeJobCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354145L;

    @ApiModelProperty("升级包ID")
    private String firmwareId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("升级范围")
    private OTAUpgradePackageJobTargetSelectionType targetSelection;
    @ApiModelProperty("批次标签")
    private List<Parameter> tagList;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("指定发起升级的时间")
    private Date scheduleTime;
    @ApiModelProperty("自动重试的时间间隔")
    private Integer retryInterval;
    @ApiModelProperty("自动重试次数")
    private Integer retryCount;
    @ApiModelProperty("设备升级超时时间")
    private Integer timeoutInMinutes;
    @ApiModelProperty("每分钟最多向多少个设备推送升级包下载URL")
    private Integer maximumPerMinute = 10000;
    @ApiModelProperty("灰度比例")
    private String grayPercent;
    @ApiModelProperty("定向升级的设备名称列表")
    private List<String> targetDeviceNameIn;
    @ApiModelProperty("结束升级的时间")
    private Date scheduleFinishTime;
    @ApiModelProperty("是否覆盖之前的升级任务")
    private Integer overwriteMode = 1;
    @ApiModelProperty("定向升级设备列表文件的URL")
    private String dnListFileUrl;
    @ApiModelProperty("分组ID")
    private String groupId;
    @ApiModelProperty("分组类型")
    private String groupType;
    @ApiModelProperty("升级包下载协议")
    private String downloadProtocol;
    @ApiModelProperty("设备是否支持多模块同时升级")
    private Boolean multiModuleMode = Boolean.FALSE;
    @ApiModelProperty("是否主动向设备推送升级任务")
    private Boolean needPush = Boolean.TRUE;
    @ApiModelProperty("是否自主控制设备OTA升级")
    private Boolean needConfirm = Boolean.FALSE;

//    private String filePath;
    @ApiModelProperty("待升级版本号列表")
    private List<String> srcVersionIn;
}


