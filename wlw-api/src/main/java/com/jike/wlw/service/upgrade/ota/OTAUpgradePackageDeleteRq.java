package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * @title: OTAUpgradePackageDeleteRq
 * @Author RS
 * @Date: 2023/3/9 15:29
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("OTA升级包删除参数")
public class OTAUpgradePackageDeleteRq  extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276222145L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("OTA升级包ID")
    private String firmwareId;
}


