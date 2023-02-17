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
@ApiModel("批量校验导入的云网关设备响应结果")
public class BatchCheckVehicleDeviceResult implements Serializable {
    private static final long serialVersionUID = -6299977399560140881L;

    @ApiModelProperty("不合法设备ID的列表")
    public List<String> InvalidDeviceIdList;
    @ApiModelProperty("不合法设备型号的列表")
    public List<String> InvalidDeviceModelList;
    @ApiModelProperty("不合法设备厂商ID的列表")
    public List<String> InvalidManufacturerList;
    @ApiModelProperty("重复设备ID的列表")
    public List<String> RepeatedDeviceIdList;
}
