package com.jike.wlw.core.upgrade.iot.entity;

import com.aliyun.iot20180120.models.CreateOTAFirmwareRequest.CreateOTAFirmwareRequestMultiFiles;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTAFirmwareRq
 * @Author RS
 * @Date: 2023/1/7 18:10
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("升级包请求参数")
public class OTAFirmwareRq implements Serializable {
    private static final long serialVersionUID = 7451112224272538946L;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("OTA升级包的版本号")
    private String destVersion;
    @ApiModelProperty("OTA升级包ID")
    private String firmwareId;
    @ApiModelProperty("OTA升级包名称")
    private String firmwareName;
    @ApiModelProperty("OTA升级包文件的URL")
    private String firmwareUrl;
    @ApiModelProperty("OTA升级包文件的签名值")
    private String firmwareSign;
    @ApiModelProperty("OTA升级包签名方法")
    private String signMethod;
    @ApiModelProperty("OTA升级包文件的大小")
    private Integer firmwareSize;
    @ApiModelProperty("产品的ProductKey")
    private String productKey;
    @ApiModelProperty("OTA升级包描述")
    private String firmwareDesc;
    @ApiModelProperty("OTA升级包类型")
    private Integer type;
    @ApiModelProperty("OTA升级模块版本号")
    private String srcVersion;
    @ApiModelProperty("OTA模块名称")
    private String moduleName;
    @ApiModelProperty("是否需要通过升级包验证")
    private boolean needToVerify;
    @ApiModelProperty("推送给设备的自定义信息")
    private String udi;
    @ApiModelProperty("每页显示的固件数量")
    private Integer pageSize;
    @ApiModelProperty("结果从第几页开始显示，页数从1开始")
    private Integer currentPage;
    @ApiModelProperty("OTA升级包")
    private List<CreateOTAFirmwareRequestMultiFiles> multiFiles;
}


