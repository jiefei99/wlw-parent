package com.jike.wlw.config.client;

import com.aliyun.teaopenapi.models.Config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private final Environment env;
    @Autowired
    public AliIotClientByConfig(Environment env) {
        this.env=env;
        this.endpoint="iot.cn-shanghai.aliyuncs.com";
        //env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret")
        this.accessKeyId=env.getProperty("ali.iot.accessKey");
        this.accessKeySecret=env.getProperty("ali.iot.accessSecret");
    }
}


