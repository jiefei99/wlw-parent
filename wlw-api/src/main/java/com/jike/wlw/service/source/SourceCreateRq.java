package com.jike.wlw.service.source;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.source.iot.AliyunSource;
import com.jike.wlw.service.source.local.InfluxSource;
import com.jike.wlw.service.source.local.MqttSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("资源信息")
public class SourceCreateRq implements Serializable {
    private static final long serialVersionUID = 6220488135956881319L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("环境")
    private SourceEvns environment;
    @ApiModelProperty("类型")
    private SourceTypes type;

    @ApiModelProperty("阿里云连接参数")
    private AliyunSource aliyunSource;
    @ApiModelProperty("mqtt连接参数")
    private MqttSource mqttSource;
    @ApiModelProperty("influx连接参数")
    private InfluxSource influxSource;
}
