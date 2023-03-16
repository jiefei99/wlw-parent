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
public class DesiredPropertyInfoDTO extends Entity implements Serializable {

    private static final long serialVersionUID = 4662576566232938440L;
    @ApiModelProperty("属性数据类型")
    public String dataType;
    @ApiModelProperty("属性标识符")
    public String identifier;
    @ApiModelProperty("属性名称")
    public String name;
    @ApiModelProperty("期望属性的修改时间，单位：ms")
    public String time;
    @ApiModelProperty("属性单位")
    public String unit;
    @ApiModelProperty("期望属性值")
    public String value;
    @ApiModelProperty("当前期望属性值的版本号")
    public Long version;
}
