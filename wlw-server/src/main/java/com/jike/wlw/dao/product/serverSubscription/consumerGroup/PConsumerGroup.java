package com.jike.wlw.dao.product.serverSubscription.consumerGroup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @title: PConsumerGroup
 * @Author RS
 * @Date: 2023/1/13 15:20
 * @Version 1.0
 */

@Getter
@Setter
public class PConsumerGroup extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264763L;

    public static final String TABLE_NAME = "wlw_consumer_group";

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("消费组Id")
    private String id;
    @ApiModelProperty("消费组名称")
    private String name;
//    @ApiModelProperty("消费组的单个客户端堆积消息消费速率")
//    private Integer accumulatedConsumeCountPerMinute;  //单位 条/分钟
//    @ApiModelProperty("在线客户端Id")
//    private String clientId;
//    @ApiModelProperty("在线客户端Id含端口")
//    private String clientIpPort;
//    @ApiModelProperty("在线客户端最后在线时间")
//    private Long onlineTime;
//    @ApiModelProperty("消费组的单个客户端实时消息消费速率")  //单位 条/分钟
//    private Integer realTimeConsumeCountPerMinute;


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


