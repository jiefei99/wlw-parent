package com.jike.wlw.service.serverSubscription.consumerGroup;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ConsumerGroupDeleteRq
 * @Author RS
 * @Date: 2023/3/6 18:13
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("删除消费组请求参数")
public class ConsumerGroupDeleteRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276344116L;

    @ApiModelProperty("名称")
    private String id;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


