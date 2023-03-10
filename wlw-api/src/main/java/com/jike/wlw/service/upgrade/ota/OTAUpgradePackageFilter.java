package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageFilter
 * @Author RS
 * @Date: 2023/3/8 17:35
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("OTA升级包请求参数")
public class OTAUpgradePackageFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7698859554276354121L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("升级包版本")
    private String destVersion;
}


