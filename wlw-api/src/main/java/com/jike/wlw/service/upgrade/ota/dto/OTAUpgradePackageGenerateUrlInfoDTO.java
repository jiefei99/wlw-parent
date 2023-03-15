package com.jike.wlw.service.upgrade.ota.dto;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAUpgradePackageGenerateUrlDTO
 * @Author RS
 * @Date: 2023/3/15 14:35
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("上传oss返回url和信息")
public class OTAUpgradePackageGenerateUrlInfoDTO extends StandardEntity {
    private static final long serialVersionUID = 6355133829103467737L;

    @ApiModelProperty("文件的URL")
    public String url;
    @ApiModelProperty("接入域名")
    public String host;
    @ApiModelProperty("调用OSS的接口PostObject上传对象（即文件）的名称，包含OSS对象的完整路径")
    public String key;
    @ApiModelProperty("对象存储类型")
    public String objectStorage;
    @ApiModelProperty("验证")
    public String policy;
    @ApiModelProperty("签名信息")
    public String signature;
    @ApiModelProperty("AccessKeyId")
    public String accessKeyId;
}


