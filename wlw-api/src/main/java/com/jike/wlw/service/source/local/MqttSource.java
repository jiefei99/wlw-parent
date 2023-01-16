package com.jike.wlw.service.source.local;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("mqtt资源")
public class MqttSource implements Serializable {
    private static final long serialVersionUID = 5622443167943852977L;

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("连接地址")
    private String hostUrl;
    @ApiModelProperty("客户Id")
    private String clientId;
    @ApiModelProperty("连接话题")
    private String topic;
    @ApiModelProperty("超时时间")
    private int timeout;
    @ApiModelProperty("保持连接数")
    private int keepalive;
    @ApiModelProperty("保持连接数")
    private boolean isAutomaticReconnect = true;
}
