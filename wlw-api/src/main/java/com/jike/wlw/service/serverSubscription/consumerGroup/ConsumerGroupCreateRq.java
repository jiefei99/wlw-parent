package com.jike.wlw.service.serverSubscription.consumerGroup;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ConsumerGroupCreateRq
 * @Author RS
 * @Date: 2023/1/13 14:31
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("创建消费组请求参数")
public class ConsumerGroupCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354116L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


