package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUpgradePackageGenerateDeviceNameListUrlRq
 * @Author RS
 * @Date: 2023/3/15 14:29
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("生成设备列表文件上传到OSS参数")
public class OTAUpgradePackageGenerateDeviceNameListUrlRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859552276354145L;

    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


