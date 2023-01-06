/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月05日 21:33 - ASUS - 创建。
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
 * @author ASUS
 * @since 1.0
 */
@Setter
@Getter
@ApiModel("物模型管理请求参数")
public class PhysicalModelManagerRq implements Serializable {
    private static final long serialVersionUID = 7456101024272538946L;
    @ApiModelProperty("查询的品类的标识符")
    private String categoryKey;
    @ApiModelProperty("是否获取精简版物模型信息")
    private boolean simple;
    @ApiModelProperty("删除的事件标识符列表")
    private List<String> eventIdentifier;
    @ApiModelProperty("删除的服务标识符列表")
    private List<String> serviceIdentifier;
    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("要复制的物模型所属产品的ProductKey")
    private String sourceProductKey;
    @ApiModelProperty("目标产品的ProductKey")
    private String targetProductKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("功能原有的标识符")
    private String identifier;
    @ApiModelProperty("查看的物模型版本号")
    private String modelVersion;
    @ApiModelProperty("资源组Id")
    private String resourceGroupId;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("物模型的自定义模块名称")
    private String functionBlockName;
    @ApiModelProperty("功能定义详情")
    private Map<String,String> thingModelJson;
}
