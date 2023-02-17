package com.jike.wlw.service.equipment.ali;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/2/17
 */
@Setter
@Getter
@ApiModel("云网关产品下单个导入设备响应结果")
public class ImportDeviceResult implements Serializable {
    private static final long serialVersionUID = 1476217716426967042L;

    @ApiModelProperty("设备名称")
    public String deviceName;
    @ApiModelProperty("设备密钥")
    public String deviceSecret;
    @ApiModelProperty("实例ID")
    public String iotId;
    @ApiModelProperty("备注名称")
    public String nickname;
    @ApiModelProperty("所属产品的ProductKey")
    public String productKey;
    @ApiModelProperty("序列号")
    public String sn;
}
