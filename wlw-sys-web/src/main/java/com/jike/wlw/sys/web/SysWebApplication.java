/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/27 13:46 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 平台小程序应用程序服务启动入口
 */
@EnableFeignClients(basePackages = {"com.jike"})
@EnableDiscoveryClient
@SpringBootApplication
public class SysWebApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SysWebApplication.class);
        springApplication.addListeners(new ApplicationStartUp());
        springApplication.run(args);
    }
}
