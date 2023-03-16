package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTAUpgradePackageCancelTaskByDeviceRq
 * @Author RS
 * @Date: 2023/3/14 11:16
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("取消指定OTA升级包的设备升级作业")
public class OTAUpgradePackageCancelTaskByDeviceRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354145L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("OTA升级包ID")
    private String firmwareId;
    @ApiModelProperty("升级批次ID")
    private String jobId;
    @ApiModelProperty("取消升级的设备名称")
    private List<String> deviceNameIn;
}


