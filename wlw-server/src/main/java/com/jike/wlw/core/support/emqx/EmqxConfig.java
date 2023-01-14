package com.jike.wlw.core.support.emqx;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MqttConfig
 * @Description: TODO
 * @Author: liujianfu
 * @Date: 2021/08/16 14:43:39
 * @Version: V1.0
 **/
@Component
@ConfigurationProperties("spring.mqtt")
@Setter
@Getter
public class EmqxConfig {
    @Autowired
    private EmqxClient emqxClient;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接地址
     */
    private String hostUrl;
    /**
     * 客户Id
     */
    private String clientId;
    /**
     * 默认连接话题
     */
    private String defaultTopic;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 保持连接数
     */
    private int keepalive;

    @Bean
    public EmqxClient getMqttPushClient() {
        emqxClient.connect(hostUrl, clientId, username, password, timeout, keepalive,defaultTopic);
        // 以/#结尾表示订阅所有以test开头的主题
        emqxClient.subscribe(defaultTopic, 1);

//        System.out.println("发送消息拉");
//        emqxClient.publish(1,false,"topic-test","sb223");
        return emqxClient;
    }
}
