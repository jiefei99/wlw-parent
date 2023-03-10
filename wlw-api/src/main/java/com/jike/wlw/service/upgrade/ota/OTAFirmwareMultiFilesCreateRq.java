package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAFirmwareMultiFilesCreateRq
 * @Author RS
 * @Date: 2023/3/9 14:46
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("创建OTA升级包文件请求参数")
public class OTAFirmwareMultiFilesCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276352245L;

    @ApiModelProperty("OTA升级包")
    public String fileMd5;
    @ApiModelProperty("OTA升级包")
    public String name;
    @ApiModelProperty("OTA升级包")
    public String signValue;
    @ApiModelProperty("OTA升级包")
    public Integer size;
    @ApiModelProperty("OTA升级包")
    public String url;
}


