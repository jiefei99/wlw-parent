package com.jike.wlw.core.support.aliamqp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ali.iot")
public class AliIotProperties {
    private String accessKey;
    private String accessSecret;
    private String clientId;
    private String instanceId;
    private String consumerGroupId;
    private String productKeys;
    private String regionId;
    private String deviceTemplate;
}
