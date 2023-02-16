package com.jike.wlw.service.topic.metrics;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ApiModel("指标数据")
@ToString
public class TopicMetrics extends Entity {

    @ApiModelProperty("时间")
    private Date createTime;
    @ApiModelProperty("Topic")
    private  String topic;
    @ApiModelProperty("metrics")
    private Metrics metrics;

}
