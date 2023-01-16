package com.jike.wlw.service.source.local;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("influx资源")
public class InfluxSource implements Serializable {
    private static final long serialVersionUID = -4314752753645037090L;
    private static final String POLICY_AUTOGEN = "autogen";

    @ApiModelProperty("连接地址及端口号")
    private String influxDBUrl;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("数据库名")
    private String database;
    @ApiModelProperty("保留策略")
    private String retentionPolicy;
    @ApiModelProperty("设置日志输出级别")
    private String logLevel;
}
