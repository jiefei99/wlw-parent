package com.jike.wlw.service.equipment;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("设备新增请求参数")
public class EquipmentCreateRq extends Entity implements Serializable {

    private static final long serialVersionUID = 2508498364862758014L;
    @ApiModelProperty("设备所属产品的productKey")  //必填
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("LoRaWAN设备的DevEUI")//创建LoRaWAN设备时，该参数必传
    private String devEui;
    @ApiModelProperty("备注名称")
    private String nickname;
    @ApiModelProperty("LoRaWAN设备的PIN Code")//用于校验DevEUI的合法性,创建LoRaWAN设备时，该参数必传
    private String pinCode;
    @ApiModelProperty("LoRaWAN设备的入网凭证JoinEui")//创建LoRaWAN设备时，LoraNodeType为USERDEFINED，该参数必传
    private String joinEui;
    @ApiModelProperty("LoRaWAN设备的AppKey")//LoRaWAN设备的AppKey
    private String appKey;
    @ApiModelProperty("LoraNodeType")
//ALIYUNDEFINED：阿里云颁发类型，需同时传入DevEui和PinCode。USERDEFINED：用户自定义类型，需同时传入DevEui、JoinEui和AppKey。
    private String loraNodeType;

}
