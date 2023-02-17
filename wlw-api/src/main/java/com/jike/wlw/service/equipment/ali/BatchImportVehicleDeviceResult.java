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
@ApiModel("云网关产品下批量导入设备响应结果")
public class BatchImportVehicleDeviceResult implements Serializable {
    private static final long serialVersionUID = -4263553774509212180L;

    @ApiModelProperty("申请批次ID")
    private Long applyId;
}
