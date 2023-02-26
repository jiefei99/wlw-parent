package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: PhysicalModelEvent
 * @Author RS
 * @Date: 2023/2/17 11:11
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("物模型事件")
public class PhysicalModelEvent extends PhysicalModelBase {
    private static final long serialVersionUID = 130312029720547601L;

    @ApiModelProperty("方法名")
    private String method;
    @ApiModelProperty("输出参数")
    private List<PhysicalModelIOParm> outputParams;
    @ApiModelProperty("事件类型")
    private EventType eventType;
}


