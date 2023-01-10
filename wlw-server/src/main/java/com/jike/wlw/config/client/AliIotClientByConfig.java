package com.jike.wlw.config.client;

import com.aliyun.teaopenapi.models.Config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
public class AliIotClientByConfig extends Config {
    @Value("${ali.iot.accessKey}")
    private String accessKey;
    @Value("${ali.iot.accessSecret}")
    private String accessSecret;

    public AliIotClientByConfig() {
        this.endpoint="iot.cn-shanghai.aliyuncs.com";
        this.accessKeyId=this.accessKey;
        this.accessKeySecret=this.accessSecret;
    }
}


