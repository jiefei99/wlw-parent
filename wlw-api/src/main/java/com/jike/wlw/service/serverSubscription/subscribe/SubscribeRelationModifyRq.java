package com.jike.wlw.service.serverSubscription.subscribe;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: SubscribeRelationModifyRq
 * @Author RS
 * @Date: 2023/1/14 9:39
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("修改订阅请求参数")
public class SubscribeRelationModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276374116L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("订阅类型")
    private String type;
    @ApiModelProperty("推送消息类型")
    private List<String> pushMessageType;
    @ApiModelProperty("MNS队列的配置信息")         //Type为MNS时必填
    private String mnsConfiguration;
    @ApiModelProperty("标识当前订阅产品的其他类型消息") //如果订阅的是JT/T 808协议云网关产品，必须传入
    private String subscribeFlags;
    @ApiModelProperty("创建的AMQP订阅中的消费组ID")   //Type为AMQP时必填。
    private List<String> consumerGroupIds;
}


