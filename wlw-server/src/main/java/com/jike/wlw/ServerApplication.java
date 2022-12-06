/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/26 21:05 - chenpeisi - 创建。
 */
package com.jike.wlw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 服务启动入口应用
 */
@Api("服务启动入口应用")
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@ImportResource("classpath:wlw-server.xml")
public class ServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ServerApplication.class);
        springApplication.addListeners(new ApplicationStartUp());
        springApplication.run(args);
    }
}