package com.jike.wlw.service.serverSubscription.subscribe;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: SubscribeRelation
 * @Author RS
 * @Date: 2023/1/14 10:28
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("服务端订阅")
public class SubscribeRelation extends StandardEntity {
    private static final long serialVersionUID = 6355685899103067787L;
    public static final String DEVICE_DATA_FLAG = "deviceDataFlag";//是否选择设备上报消息
    public static final String DEVICE_STATUS_CHANGE_FLAG = "deviceStatusChangeFlag";//是否选择设备状态变化通知
    public static final String DEVICE_TOPO_LIFE_CYCLE_FLAG = "deviceTopoLifeCycleFlag";//是否选择设备拓扑关系变更（仅对网关产品有效）
    public static final String FOUND_DEVICE_LIST_FLAG = "foundDeviceListFlag";//是否选择网关子设备发现上报（仅对网关产品有效）
    public static final String THING_HISTORY_FLAG = "thingHistoryFlag";//是否选择物模型历史数据上报
    public static final String DEVICE_LIFE_CYCLE_FLAG = "deviceLifeCycleFlag";//是否选择设备生命周期变更
    public static final String OTA_EVENT_FLAG = "otaEventFlag";//是否选择OTA升级状态通知
    public static final String DEVICE_TAG_FLAG = "deviceTagFlag";//是否选择设备标签变更（仅当Type为AMQP时有效。）
    public static final String OTA_VERSION_FLAG = "otaVersionFlag";//否选择OTA模块版本号上报（仅当Type为AMQP时有效。）
    public static final String OTA_JOB_FLAG = "otaJobFlag";//否选择OTA升级批次状态通知（仅当Type为AMQP时有效。）
    public static final String AMQP = "AMQP";
    public static final String ADD = "add";
    public static final String DEL = "del";

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("标识当前订阅产品的其他类型消息")
    private String subscribeFlags;
    @ApiModelProperty("创建的AMQP订阅中的消费组ID")
    private List<String> consumerGroupIds;
    @ApiModelProperty("订阅类型")
    private String type;
    @ApiModelProperty("MNS队列的配置信息")
    private String mnsConfiguration;
    @ApiModelProperty("推送消息类型")
    private List<String> pushMessageType;
}


