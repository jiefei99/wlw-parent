package com.jike.wlw.core.upgrade.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: OTAUploadRq
 * @Author RS
 * @Date: 2023/1/9 15:39
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("升级包上传请求参数")
public class OTAUploadRq implements Serializable {
    private static final long serialVersionUID = 7451222224272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("升级包文件拓展名")
    private String fileSuffix;
}


