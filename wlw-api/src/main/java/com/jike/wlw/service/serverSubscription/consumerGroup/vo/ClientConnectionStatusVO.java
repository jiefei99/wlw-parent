package com.jike.wlw.service.serverSubscription.consumerGroup.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @title: ClientConnectionStatusVO
 * @Author RS
 * @Date: 2023/3/7 16:20
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("消费组在线客户端信息")
public class ClientConnectionStatusVO  extends StandardEntity {
    private static final long serialVersionUID = 6355684899103467737L;

    @ApiModelProperty("消费组的单个客户端堆积消息消费速率")
    private Integer accumulatedConsumeCountPerMinute;
    @ApiModelProperty("在线客户端ID")
    private String clientId;
    @ApiModelProperty("在线客户端IP和端口")
    private String clientIpPort;
    @ApiModelProperty("在线客户端的最后上线时间。")
    private Date onlineTime;
    @ApiModelProperty("消费组的单个客户端实时消息消费速率")
    private Integer realTimeConsumeCountPerMinute;
}


