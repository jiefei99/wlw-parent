package com.jike.wlw.service.product.serverSubscription.consumerGroup;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: ConsumerGroup
 * @Author RS
 * @Date: 2023/1/13 14:16
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("消费组")
public class ConsumerGroup extends StandardEntity {

    private static final long serialVersionUID = 6355685899103067787L;

    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("消费组ID")
    private String groupId;
    @ApiModelProperty("消费组名称")
    private String groupName;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("创建时间")
    private String createTime;
//状态
    @ApiModelProperty("消费组的单个客户端堆积消息消费速率")
    private Integer accumulatedConsumeCountPerMinute;  //单位 条/分钟
    @ApiModelProperty("在线客户端Id")
    private String clientId;
    @ApiModelProperty("在线客户端Id含端口")
    private String clientIpPort;
    @ApiModelProperty("在线客户端最后在线时间")
    private Long onlineTime;
    @ApiModelProperty("消费组的单个客户端堆积消息消费速率")  //单位 条/分钟
    private Integer realTimeConsumeCountPerMinute;

}


