package com.jike.wlw.service.source;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.source.iot.AliyunSource;
import com.jike.wlw.service.source.local.InfluxSource;
import com.jike.wlw.service.source.local.MqttSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("资源信息")
public class Source extends StandardEntity {
    private static final long serialVersionUID = 664293227949105584L;

    public static final String EVN_CLOUD = "cloud";
    public static final String EVN_LOCAL = "local";
    public static final String TYPE_ALIYUN = "aliyun";
    public static final String TYPE_MQTT = "mqtt";
    public static final String TYPE_INFLUX = "influx";

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("环境")
    private String environment;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("是否删除")
    private Boolean deleted;
    @ApiModelProperty("是否连接")
    private Boolean connected;

    @ApiModelProperty("阿里云连接参数")
    private AliyunSource aliyunSource;
    @ApiModelProperty("mqtt连接参数")
    private MqttSource mqttSource;
    @ApiModelProperty("influx连接参数")
    private InfluxSource influxSource;
}
