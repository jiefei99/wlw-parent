package com.jike.wlw.service.equipment.ali.dto;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("设备属性期望值返回参数")
public class PropertyInfoDTO extends Entity implements Serializable {

    private static final long serialVersionUID = 4662576566232938440L;

    @ApiModelProperty("属性修改时间")
    public String time;
    @ApiModelProperty("属性值")
    public String value;
}
