package com.jike.wlw.service.serverSubscription.consumerGroup;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ConsumerGroupModifyRq
 * @Author RS
 * @Date: 2023/1/13 14:35
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("修改消费组请求参数")
public class ConsumerGroupModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354117L;

    @ApiModelProperty("名称")
    private String groupName;
    @ApiModelProperty("消费组ID")
    private String groupId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


