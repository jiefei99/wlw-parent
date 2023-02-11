package com.jike.wlw.service.source;

import com.jike.wlw.service.source.iot.AliyunSource;
import com.jike.wlw.service.source.local.InfluxSource;
import com.jike.wlw.service.source.local.MqttSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/2/11
 */
@Setter
@Getter
@ApiModel("资源连接参数")
public class SourceInfo implements Serializable {
    private static final long serialVersionUID = 917901972597828049L;

    @ApiModelProperty("阿里云连接参数")
    private AliyunSource aliyunSource;
    @ApiModelProperty("mqtt连接参数")
    private MqttSource mqttSource;
    @ApiModelProperty("influx连接参数")
    private InfluxSource influxSource;
}
