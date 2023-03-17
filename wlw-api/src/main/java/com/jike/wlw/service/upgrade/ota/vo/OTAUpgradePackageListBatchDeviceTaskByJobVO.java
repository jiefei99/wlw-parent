package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class OTAUpgradePackageListBatchDeviceTaskByJobVO extends StandardEntity {
    @ApiModelProperty("总数")
    private Long total=0L;
    @ApiModelProperty("待确认")
    private Long confirmTotal=0L;
    @ApiModelProperty("待推送")
    private Long queuedTotal=0L;
    @ApiModelProperty("已推送")
    private Long notifiedTotal=0L;
    @ApiModelProperty("升级中")
    private Long upgradingTotal=0L;
    @ApiModelProperty("升级成功")
    private Long successTotal=0L;
    @ApiModelProperty("升级失败")
    private Long failedTotal=0L;
    @ApiModelProperty("已取消")
    private Long canceledTotal=0L;
}


