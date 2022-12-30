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
public class FunctionModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("读写类型")
    private AccessMode accessMode;
    @ApiModelProperty("是否是标准功能的标准属性")
    private Boolean required;
}
