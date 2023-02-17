package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel("物模型")
public class PhysicalModel extends Entity {
    private static final long serialVersionUID = 130312029720547601L;
    public static final String CATEGORYNAME = "categoryName"; //年 2021
    public static final String CATEGORYKEY = "categoryKey"; //年 2021

    @ApiModelProperty("物模型编号")
    private String id;
    @ApiModelProperty("物模型名称")
    private String name;
    @ApiModelProperty("功能编号集合")
    private List<String> functionIds;

    @ApiModelProperty("版本")
    private String version;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("描述")
    private String desc;
    @ApiModelProperty("服务")
    private List<Object> services;
    @ApiModelProperty("事件")
    private List<Object> events;
    @ApiModelProperty("属性")
    private List<Object> properties;
}
