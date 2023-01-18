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
    public static final String DEVICE_DATA_FLAG = "deviceDataFlag";
    public static final String DEVICE_STATUS_CHANGE_FLAG = "deviceStatusChangeFlag";
    public static final String DEVICE_TOPO_LIFE_CYCLE_FLAG = "deviceTopoLifeCycleFlag";
    public static final String FOUND_DEVICE_LIST_FLAG = "foundDeviceListFlag";
    public static final String THING_HISTORY_FLAG = "thingHistoryFlag";
    public static final String DEVICE_LIFE_CYCLE_FLAG = "deviceLifeCycleFlag";
    public static final String OTA_EVENT_FLAG = "otaEventFlag";
    public static final String DEVICE_TAG_FLAG = "deviceTagFlag";
    public static final String OTA_VERSION_FLAG = "otaVersionFlag";
    public static final String OTA_JOB_FLAG = "otaJobFlag";
    public static final String AMQP = "AMQP";

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

//    @ApiModelProperty("是否选择设备上报消息")
//    private boolean deviceDataFlag=false;
//    @ApiModelProperty("是否选择设备状态变化通知")
//    private boolean deviceStatusChangeFlag=false;
//    @ApiModelProperty("是否选择设备拓扑关系变更")    //true：是。仅对网关产品有效。
//    private boolean deviceTopoLifeCycleFlag=false;
//    @ApiModelProperty("是否选择网关子设备发现上报")  //true：是。仅对网关产品有效。
//    private boolean foundDeviceListFlag=false;
//    @ApiModelProperty("是否选择物模型历史数据上报")
//    private boolean thingHistoryFlag=false;
//    @ApiModelProperty("是否选择设备生命周期变更")
//    private boolean deviceLifeCycleFlag=false;
//    @ApiModelProperty("是否选择OTA升级状态通知")
//    private boolean otaEventFlag=false;
//    @ApiModelProperty("是否选择设备标签变更")       //true：是。仅当Type为AMQP时有效。
//    private boolean deviceTagFlag=false;
//    @ApiModelProperty("否选择OTA模块版本号上报")    //true：是。仅当Type为AMQP时有效。
//    private boolean otaVersionFlag=false;
//    @ApiModelProperty("否选择OTA升级批次状态通知")  //true：是。仅当Type为AMQP时有效。
//    private boolean otaJobFlag=false;

}


