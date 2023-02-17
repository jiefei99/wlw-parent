package com.jike.wlw.service.equipment.ali;

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
@Setter
@Getter
@ApiModel("批量校验导入的设备响应结果")
public class BatchCheckImportDeviceResult implements Serializable {
    private static final long serialVersionUID = -4000894394670046645L;

    @ApiModelProperty("不合法设备名称列表")
    public List<String> invalidDeviceNameList;
    @ApiModelProperty("不合法设备密钥的列表")
    public List<String> invalidDeviceSecretList;
    @ApiModelProperty("不合法设备序列号的列表")
    public List<String> invalidSnList;
    @ApiModelProperty("重复设备名称的列表")
    public List<String> repeatedDeviceNameList;
}
