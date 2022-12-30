package com.jike.wlw.service.physicalmodel.function;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class FunctionCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型")
    private FunctionType type;
    @ApiModelProperty("读写类型")
    private AccessMode accessMode;
    @ApiModelProperty("是否是标准功能的标准属性")
    private boolean required;

    //属性类型
    @ApiModelProperty("期望值类型")
    private ValueType valueType;
    @ApiModelProperty("期望值")
    private Object value;
//    @ApiModelProperty("时间")
//    private Date occurTime;

    //事件、服务类型
    @ApiModelProperty("事件描述/服务描述")
    private String desc;
    @ApiModelProperty("出参")
    private FunctionData outputData;
    @ApiModelProperty("入参")  //事件类型独有
    private FunctionData inputData;
}
