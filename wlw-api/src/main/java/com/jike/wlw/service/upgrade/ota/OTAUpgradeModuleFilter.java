package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradeModuleFilter
 * @Author RS
 * @Date: 2023/3/9 15:23
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("OTA升级模块请求参数")
public class OTAUpgradeModuleFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7698859554276354122L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("productKey")
    private String productKey;
}


