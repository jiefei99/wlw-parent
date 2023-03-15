package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUpgradePackageCancelStrategyByJobRq
 * @Author RS
 * @Date: 2023/3/15 11:52
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("取消动态升级策略参数")
public class OTAUpgradePackageCancelStrategyByJobRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7118859154273354145L;

    @ApiModelProperty("升级批次ID")
    private String jobId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


