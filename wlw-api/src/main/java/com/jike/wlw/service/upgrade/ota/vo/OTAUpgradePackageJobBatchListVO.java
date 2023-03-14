package com.jike.wlw.service.upgrade.ota.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobSelectionType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageJobListVO
 * @Author RS
 * @Date: 2023/3/14 9:35
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("OTA升级包任务批次")
public class OTAUpgradePackageJobBatchListVO extends StandardEntity {
    private static final long serialVersionUID = 6355633829103467737L;

    @ApiModelProperty("升级批次Id")
    private String jobId;
    @ApiModelProperty("升级批次类型")
    private OTAUpgradePackageJobType jobType;
    @ApiModelProperty("策略")
    private OTAUpgradePackageJobSelectionType selectionType;
    @ApiModelProperty("状态")
    private OTAUpgradePackageJobStatusType jobStatusType;
    @ApiModelProperty("ProductKey")
    private String productKey;
}


