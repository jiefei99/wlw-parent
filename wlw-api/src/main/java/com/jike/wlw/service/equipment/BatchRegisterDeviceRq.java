package com.jike.wlw.service.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wza
 * @create 2023/2/17
 */
@Getter
@Setter
@ApiModel
public class BatchRegisterDeviceRq implements Serializable {
    private static final long serialVersionUID = -8580030149082080444L;

    @ApiModelProperty("注册的设备数量")
    public Integer count;
    @ApiModelProperty("ProductKey")
    public String productKey;
    @ApiModelProperty("iotInstanceId")
    public String iotInstanceId;
}



