package com.jike.wlw.config.client;

import com.aliyun.iot20180120.Client;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: AliIOTClient
 * @Author RS
 * @Date: 2023/1/9 9:14
 * @Version 1.0
 */

@Component
@Getter
@Setter
public class AliIotClient extends Client {

    @Autowired
    public AliIotClient(AliIotClientByConfig aliIotConfig) throws Exception {
        super(aliIotConfig);
    }
}


