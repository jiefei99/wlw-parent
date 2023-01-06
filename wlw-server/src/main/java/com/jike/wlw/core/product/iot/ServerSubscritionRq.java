/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月06日 17:17 - ASUS - 创建。
 */
package com.jike.wlw.core.product.iot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author rs
 * @since 1.0
 */

@Setter
@Getter
@ApiModel("服务端订阅请求参数")
public class ServerSubscritionRq implements Serializable {
    private static final long serialVersionUID = 7451101024072538946L;

    @ApiModelProperty("该订阅中的产品的ProductKey")
    private String productKey;
    @ApiModelProperty("消费组ID")
    private String groupId;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("指定显示返回结果中的第几页,最小值为1")
    public String currentPage;
    @ApiModelProperty("指定返回结果中每页显示的消费组数量，最小值为1，最大值为1000")
    public String pageSize;
    @ApiModelProperty("是否使用模糊查询")
    public boolean fuzzy=false;
    @ApiModelProperty("消费组名称")
    public String groupName;
    @ApiModelProperty("订阅类型")
    public String type;
    @ApiModelProperty("是否选择设备上报消息")
    public boolean deviceDataFlag=false;
    @ApiModelProperty("是否选择设备状态变化通知")
    public boolean deviceStatusChangeFlag=false;
    @ApiModelProperty("是否选择网关子设备发现上报")
    public boolean foundDeviceListFlag=false;
    @ApiModelProperty("是否选择设备拓扑关系变更")
    public boolean deviceTopoLifeCycleFlag=false;
    @ApiModelProperty("是否选择设备生命周期变更")
    public boolean deviceLifeCycleFlag=false;
    @ApiModelProperty("是否选择物模型历史数据上报")
    public boolean thingHistoryFlag=false;
    @ApiModelProperty("是否选择OTA升级状态通知")
    public boolean otaEventFlag=false;
    @ApiModelProperty("是否选择设备标签变更")
    public boolean deviceTagFlag=false;
    @ApiModelProperty("是否选择OTA模块版本号上报")
    public boolean otaVersionFlag=false;
    @ApiModelProperty("是否选择OTA升级批次状态通知")
    public boolean otaJobFlag=false;
    @ApiModelProperty("MNS队列的配置信息，Type为MNS时必填。")
    public String mnsConfiguration;
    @ApiModelProperty("创建的AMQP订阅中的消费组ID")
    public List<String> consumerGroupIdList;
}
