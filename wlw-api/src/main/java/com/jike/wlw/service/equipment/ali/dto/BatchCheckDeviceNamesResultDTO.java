package com.jike.wlw.service.equipment.ali.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel("批量校验设备名称返回参数")
public class BatchCheckDeviceNamesResultDTO implements Serializable {
    private static final long serialVersionUID = 2855705213759722892L;

    @ApiModelProperty("申请批次Id")
    public String applyId;
    @ApiModelProperty("不合法的设备名称列表")
    public List<String> invalidDeviceNameList;
    @ApiModelProperty("不合法的设备备注名称列表")
    public List<String> invalidDeviceNicknameList;
}
