/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月05日 16:24 - ASUS - 创建。
 */
package com.jike.wlw.core.physicalmodel.iot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author rs
 * @since 1.0
 */
@Setter
@Getter
@ApiModel("物模型使用请求参数")
public class PhysicalModelUseRq implements Serializable {
    private static final long serialVersionUID = 7456101024172538946L;
    @ApiModelProperty("排序方式  0：倒序 1：正序")
    private Integer asc;
    @ApiModelProperty("事件类型   info：信息   alert：告警   error：故障")
    private String eventType;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("属性记录的结束时间")
    private String endTime;
    @ApiModelProperty("属性记录的开始时间")
    private String startTime;
    @ApiModelProperty("每页记录数")
    private String pageSize;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("设备名称集合")
    private List<String> deviceNameList;
    @ApiModelProperty("设备ID")
    private String iotId;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("服务的标识符")
    private String identifier;
    @ApiModelProperty("服务的标识符")
    private List<String> identifierList;
    @ApiModelProperty("启用服务的入参信息")
    private Map<String,String> args;
    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("设置的属性信息")
    private Map<String,String> items;
    @ApiModelProperty("期望属性值版本")
    private Map<String,String> versions;

}
