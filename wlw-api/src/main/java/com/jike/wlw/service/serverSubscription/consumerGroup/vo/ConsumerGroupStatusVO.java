package com.jike.wlw.service.serverSubscription.consumerGroup.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @title: ConsumerGroupStatusVO
 * @Author RS
 * @Date: 2023/3/7 16:09
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("消费组状态")
public class ConsumerGroupStatusVO extends StandardEntity {
    private static final long serialVersionUID = 6355684899103067737L;

    @ApiModelProperty("消费组堆积消息消费速率")
    private Integer accumulatedConsumeCountPerMinute;
    @ApiModelProperty("消费组消息堆积数")
    private Integer accumulationCount;
    @ApiModelProperty("消费组消息消费速率")
    private Integer consumerSpeed;
    @ApiModelProperty("最近消息消费时间")
    private Date lastConsumerDateTime;//为UTC时间，以毫秒计 要给他做一次转换
    @ApiModelProperty("消费组实时消息消费速率")
    private Integer realTimeConsumeCountPerMinute;
    @ApiModelProperty("消费组的在线客户端信息")
    private List<ClientConnectionStatusVO> clientStatusList;
}


