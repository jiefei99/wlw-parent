package com.jike.wlw.service.topic.metrics;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel("指标数据")
@ToString
public class TopicMetrics extends Entity {

    @ApiModelProperty("Topic")
    private String topic;
    @ApiModelProperty("metrics")
    private Metrics metrics;
}
