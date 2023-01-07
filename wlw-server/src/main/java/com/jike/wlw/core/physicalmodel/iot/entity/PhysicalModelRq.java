package com.jike.wlw.core.physicalmodel.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @title: PhysicalModelRq
 * @Author RS
 * @Date: 2023/1/7 16:39
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("物模型请求参数")
public class PhysicalModelRq implements Serializable {
    private static final long serialVersionUID = 7451101024272538946L;

    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("功能定义详情")
    private Map<String,Object> thingModelJson;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("物模型的自定义模块名称")
    private String functionBlockName;
    @ApiModelProperty("功能原有的标识符")
    private String identifier;
    @ApiModelProperty("查看的物模型版本号")
    private String modelVersion;
    @ApiModelProperty("资源组Id")
    private String resourceGroupId;

    @ApiModelProperty("目标产品的ProductKey")
    private String targetProductKey;

    @ApiModelProperty("删除的事件标识符列表")
    private List<String> eventIdentifier;
    @ApiModelProperty("删除的服务标识符列表")
    private List<String> serviceIdentifier;
    @ApiModelProperty("删除的属性标识符列表。")
    private List<String> propertyIdentifier;
    @ApiModelProperty("是否获取精简版物模型信息")
    private boolean simple=false;
}


