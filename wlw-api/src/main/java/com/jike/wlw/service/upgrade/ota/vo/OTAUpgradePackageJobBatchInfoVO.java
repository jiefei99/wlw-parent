package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobSelectionType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobTargetSelectionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @title: OTAUpgradePackageJobBatchInfoVO
 * @Author RS
 * @Date: 2023/3/14 10:08
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA升级包升级批次的详情")
public class OTAUpgradePackageJobBatchInfoVO extends StandardEntity {
    private static final long serialVersionUID = 6355633899103337737L;

    @ApiModelProperty("批次ID")
    private String jobId;
    @ApiModelProperty("产品")
    private String productKey;
    @ApiModelProperty("升级包版本号")
    private String destVersion;
    @ApiModelProperty("待升级版本号")
    private List<String> srcVersionList;
    @ApiModelProperty("升级策略")
    private OTAUpgradePackageJobSelectionType selectionType;
    @ApiModelProperty("升级范围")
    private OTAUpgradePackageJobTargetSelectionType targetSelectionType;
    @ApiModelProperty("升级时间")
    private Date utcStartDate;
    @ApiModelProperty("升级包状态")
    private OTAUpgradePackageJobStatusType jobStatusType;
    @ApiModelProperty("升级包推送速率")
    private Integer maximumPerMinute;
    @ApiModelProperty("升级失败重试间隔")
    private Integer retryInterval;
    @ApiModelProperty("云端主动推送升级")
    private boolean needPush;
    @ApiModelProperty("APP确认升级")
    private boolean needConfirm;
    @ApiModelProperty("下载协议")
    private String downloadProtocol;
}


