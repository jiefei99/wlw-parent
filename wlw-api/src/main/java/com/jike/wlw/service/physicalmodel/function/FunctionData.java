package com.jike.wlw.service.physicalmodel.function;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("功能出入参")
public class FunctionData implements Serializable {
    private static final long serialVersionUID = -1237947169436819495L;

    @ApiModelProperty("编号")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("期望值类型")
    private ValueType valueType;
    @ApiModelProperty("期望值")
    private Object value;


}
