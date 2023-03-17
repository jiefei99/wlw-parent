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
@ApiModel("根据申请批次ID（ApplyId）批量注册设备请求参数")
public class BatchRegisterDeviceWithApplyIdRq implements Serializable {
    private static final long serialVersionUID = -8580030149082080444L;

    @ApiModelProperty("申请批次Id")
    public Long applyId;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
}



