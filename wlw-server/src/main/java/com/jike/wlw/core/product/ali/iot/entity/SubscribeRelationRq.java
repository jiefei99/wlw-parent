package com.jike.wlw.core.product.ali.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: SubscribeRelationRq
 * @Author RS
 * @Date: 2023/1/7 17:52
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("服务端订阅请求参数")
public class SubscribeRelationRq implements Serializable {
    private static final long serialVersionUID = 7451111124272538946L;

    @ApiModelProperty("该订阅中的产品的ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("订阅类型")
    private String type;
    @ApiModelProperty("是否选择设备上报消息")
    private boolean deviceDataFlag=false;
    @ApiModelProperty("是否选择设备状态变化通知")
    private boolean deviceStatusChangeFlag=false;
    @ApiModelProperty("是否选择网关子设备发现上报")
    private boolean foundDeviceListFlag=false;
    @ApiModelProperty("是否选择设备拓扑关系变更")
    private boolean deviceTopoLifeCycleFlag=false;
    @ApiModelProperty("是否选择设备生命周期变更")
    private boolean deviceLifeCycleFlag=false;
    @ApiModelProperty("是否选择物模型历史数据上报")
    private boolean thingHistoryFlag=false;
    @ApiModelProperty("是否选择OTA升级状态通知")
    private boolean otaEventFlag=false;
    @ApiModelProperty("是否选择设备标签变更")
    private boolean deviceTagFlag=false;
    @ApiModelProperty("是否选择OTA模块版本号上报")
    private boolean otaVersionFlag=false;
    @ApiModelProperty("是否选择OTA升级批次状态通知")
    private boolean otaJobFlag=false;
    @ApiModelProperty("MNS队列的配置信息，Type为MNS时必填。")
    private String mnsConfiguration;
    @ApiModelProperty("创建的AMQP订阅中的消费组ID")
    private List<String> consumerGroupIdList;
}


