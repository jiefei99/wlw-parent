package com.jike.wlw;

import com.alibaba.fastjson.JSON;
import com.jike.wlw.core.serverSubscription.subscribe.privatization.PrivateSubscribeRelationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @title: StartInitializer
 * @Author RS
 * @Date: 2023/1/31 16:30
 * @Version 1.0
 */
@Component
@Order(1)
public class StartInitializer implements CommandLineRunner {
    @Autowired
    private PrivateSubscribeRelationServiceImpl privateSubscribeRelationService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("项目启动,重新启动消息订阅。。。");
//        privateSubscribeRelationService.restartSubscription();
    }
}


