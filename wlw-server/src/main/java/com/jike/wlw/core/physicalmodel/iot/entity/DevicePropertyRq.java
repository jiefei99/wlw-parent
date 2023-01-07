package com.jike.wlw.core.physicalmodel.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: DevicePropertyRq
 * @Author RS
 * @Date: 2023/1/7 17:32
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("设备属性值请求参数")
public class DevicePropertyRq implements Serializable {
    private static final long serialVersionUID = 7451111024272538946L;

    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("设备ID")
    private String iotId;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("设置的属性信息")
    private Map<String,Object> items;
    @ApiModelProperty("设备名称集合")
    private List<String> deviceNameList;
    @ApiModelProperty("服务的标识符")
    private String identifier;
    @ApiModelProperty("属性记录的结束时间")
    private String endTime;
    @ApiModelProperty("属性记录的开始时间")
    private String startTime;
    @ApiModelProperty("排序方式  0：倒序 1：正序")
    private Integer asc;
    @ApiModelProperty("每页记录数")
    private String pageSize;
    @ApiModelProperty("服务的标识符")
    private List<String> identifierList;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("期望属性值版本")
    private Map<String,Object> versions=new HashMap<>();
    @ApiModelProperty("启用服务的入参信息")
    private Map<String,Object> args=new HashMap<>();
    @ApiModelProperty("事件类型   info：信息   alert：告警   error：故障")
    private String eventType;
}


