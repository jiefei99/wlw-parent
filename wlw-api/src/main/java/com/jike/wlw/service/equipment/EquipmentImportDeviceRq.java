package com.jike.wlw.service.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/2/17
 */
@Getter
@Setter
@ApiModel
public class EquipmentImportDeviceRq implements Serializable {

    private static final long serialVersionUID = 5245253104730422356L;

    @ApiModelProperty("设备名称")
    public String deviceName;
    @ApiModelProperty("设备密钥")
    public String deviceSecret;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("备注名称")
    public String nickname;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;
    @ApiModelProperty("序列号")
    public String sn;

}
