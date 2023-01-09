package com.jike.wlw.config.client;

import com.aliyun.iot20180120.Client;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import com.aliyun.teaopenapi.models.Config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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
    public AliIotClient(AliIotConfig aliIotConfig) throws Exception {
        super(aliIotConfig);
    }
}


