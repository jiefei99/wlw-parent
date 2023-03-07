package com.jike.wlw.service.serverSubscription.consumerGroup.vo;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: ConsumerGroup
 * @Author RS
 * @Date: 2023/3/6 15:11
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("消费组")
public class ConsumerGroupVO extends StandardEntity {

    private static final long serialVersionUID = 6355684899103067787L;

    private String name;
    private String id;
}


