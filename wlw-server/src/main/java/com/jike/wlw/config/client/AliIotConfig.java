package com.jike.wlw.config.client;

import com.aliyun.teaopenapi.models.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @title: AliIotConfig
 * @Author RS
 * @Date: 2023/1/9 9:18
 * @Version 1.0
 */

@Getter
@Setter
@Component
public class AliIotConfig extends Config {
    @Value("${ali.iot.accessKey}")
    private String accessKey;
    @Value("${ali.iot.accessSecret}")
    private String accessSecret;

    public AliIotConfig() {
        this.endpoint="iot.cn-shanghai.aliyuncs.com";
        this.accessKeyId=this.accessKey;
        this.accessKeySecret=this.accessSecret;
    }
}


