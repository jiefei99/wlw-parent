package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageJobByFirmwareFilter
 * @Author RS
 * @Date: 2023/3/14 9:41
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("OTA升级包任务批次请求参数")
public class OTAUpgradePackageJobByFirmwareFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7698859554276354121L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("升级包Id")
    private String firmwareId;
}


