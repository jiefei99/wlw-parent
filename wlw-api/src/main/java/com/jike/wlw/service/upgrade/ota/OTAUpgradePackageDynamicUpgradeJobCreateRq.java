package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.base.Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTAUpgradePackageDynamicUpgradeJobCreateRq
 * @Author RS
 * @Date: 2023/3/15 11:58
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("创建动态升级批次参数")
public class OTAUpgradePackageDynamicUpgradeJobCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354145L;

    @ApiModelProperty("升级包ID")
    private String firmwareId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("批次标签")
    private List<Parameter> tagList;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("自动重试的时间间隔")
    private Integer retryInterval;
    @ApiModelProperty("自动重试次数")
    private Integer retryCount;
    @ApiModelProperty("设备升级超时时间")
    private Integer timeoutInMinutes;
    @ApiModelProperty("每分钟最多向多少个设备推送升级包下载URL")
    private Integer maximumPerMinute = 10000;
    @ApiModelProperty("动态升级模式")
    private Integer dynamicMode = 1;
    @ApiModelProperty("是否覆盖之前的升级任务")
    private Integer overwriteMode = 1;
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
    //注意事项：全量或者灰度升级才可以使用此参数（如果是差分升级这个参数使用的得是待升级版本号），定向和分组升级不能使用此参数
    //        不能重复，且最多传入10个
    @ApiModelProperty("待升级版本号列表")
    private List<String> srcVersionIn;
}


