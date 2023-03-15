package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUpgradePackageGenerateOTAUploadUrl
 * @Author RS
 * @Date: 2023/3/15 14:30
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("升级包文件上传到OSS参数")
public class OTAUpgradePackageGenerateUrlRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698852552276354145L;

    @ApiModelProperty("文件扩展名")
    private String fileSuffix;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;

}


