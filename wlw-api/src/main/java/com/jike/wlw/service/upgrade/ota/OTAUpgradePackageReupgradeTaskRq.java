package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTAUpgradePackageReupgradeTaskRq
 * @Author RS
 * @Date: 2023/3/15 11:24
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("OTA升级包重新发起任务参数")
public class OTAUpgradePackageReupgradeTaskRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354145L;

    @ApiModelProperty("升级批次ID")
    private String jobId;
    @ApiModelProperty("待重新升级的设备升级作业ID")
    private List<String> taskIdIn;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


