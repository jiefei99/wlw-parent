package com.jike.wlw.service.topic.metrics;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ApiModel("Metrics数据")
@ToString
public class Metrics extends Entity {

    @ApiModelProperty("QoS 2消息五秒内平均发送速率")
    private  int messagesQos2OutRate;
    @ApiModelProperty("QoS 2消息发送数量统计")
    private  int messagesQos2OutCount;
    @ApiModelProperty("QoS 2消息五秒内平均接收速率")
    private  int messagesQos2InRate;
    @ApiModelProperty("QoS 2消息接收数量统计")
    private  int messagesQos2InCount;
    @ApiModelProperty("QoS 1消息发送数量统计")
    private  int messagesQos1OutCount;
    @ApiModelProperty("QoS 1消息五秒内平均接收速率")
    private  int messagesQos1InRate;
    @ApiModelProperty("QoS 1消息五秒内平均发送速率")
    private  int messagesQos1OutRate;
    @ApiModelProperty("QoS 1消息接收数量统计")
    private  int messagesQos1InCount;
    @ApiModelProperty("QoS 0消息发送数量统计")
    private  int messagesQos0OutCount;
    @ApiModelProperty("QoS 0消息五秒内平均接收速率")
    private  int messagesQos0InRate;
    @ApiModelProperty("QoS 0消息五秒内平均发送速率")
    private  int messagesQos0OutRate;
    @ApiModelProperty("QoS 0消息接收数量统计")
    private  int messagesQos0InCount;
    @ApiModelProperty("QoS MQTT消息发送数量统计")
    private  int messagesOutCount;
    @ApiModelProperty("QoS MQTT消息五秒内平均接收速率")
    private  int messagesInRate;
    @ApiModelProperty("Qo SMQTT消息五秒内平均发送速率")
    private  int messagesOutRate;
    @ApiModelProperty("QoS MQTT消息接收数量统计")
    private  int messagesInCount;
    @ApiModelProperty("QoS MQTT消息5秒平均丢弃速率")
    private  int messagesDroppedRate;

}
