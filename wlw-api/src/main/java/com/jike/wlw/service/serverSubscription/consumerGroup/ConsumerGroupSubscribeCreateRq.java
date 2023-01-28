package com.jike.wlw.service.serverSubscription.consumerGroup;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ConsumerGroupSubscribeRelationRq
 * @Author RS
 * @Date: 2023/1/13 14:41
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("创建消费组订阅请求参数")
public class ConsumerGroupSubscribeCreateRq  extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354120L;

    @ApiModelProperty("消费组ID")
    private String groupId;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


