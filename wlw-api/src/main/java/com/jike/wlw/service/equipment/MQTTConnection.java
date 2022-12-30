package com.jike.wlw.service.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("MQTT连接参数")
public class MQTTConnection implements Serializable {
    private static final long serialVersionUID = 3926984262829840487L;
    
    @ApiModelProperty("客户端编号")
    private String clientId;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密钥")
    private String passwd;
    @ApiModelProperty("MQTT主机链接地址")
    private String mqttHostUrl;
    @ApiModelProperty("端口号")
    private String port;

    
    
    
}
