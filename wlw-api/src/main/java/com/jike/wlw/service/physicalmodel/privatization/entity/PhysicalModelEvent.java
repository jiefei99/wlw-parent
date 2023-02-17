package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelEvent
 * @Author RS
 * @Date: 2023/2/17 11:11
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("物模型事件")
public class PhysicalModelEvent extends Entity {
    private static final long serialVersionUID = 130312029720547601L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("事件类型")
    private EventType type;
    @ApiModelProperty("物模型Id")
    private String modelDeviceId;
    @ApiModelProperty("方法名")
    private String method;
    @ApiModelProperty("描述")
    private String details;
}


