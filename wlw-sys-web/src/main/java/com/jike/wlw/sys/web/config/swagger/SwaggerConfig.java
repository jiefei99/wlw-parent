/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	wanxin-wechatweb
 * 文件名：	SwaggerConfig.java
 * 模块说明：
 * 修改历史：
 * 2018年7月8日 - lsz - 创建。
 */
package com.jike.wlw.sys.web.config.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lsz
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket createRestApi() {
        // 可以添加多个header或参数

        Docket docket = new Docket(DocumentationType.SWAGGER_2)//
                .apiInfo(apiInfo())//
                .tags(new Tag("web", "物联网云平台后台服务接口"))//
                .select()//
                .apis(RequestHandlerSelectors.basePackage("com.jike.wlw.sys.web.controller"))// REST接口
                .paths(paths())//
                .build();//

        List<ResponseMessage> messageList = new ArrayList<ResponseMessage>();
        messageList.add(new ResponseMessageBuilder().code(500).message("业务错误").build());
        messageList.add(new ResponseMessageBuilder().code(403).message("禁止访问").build());
        docket.globalResponseMessage(RequestMethod.GET, messageList);
        docket.globalResponseMessage(RequestMethod.POST, messageList);
        return docket;
    }

    private Predicate<String> paths() {
        return Predicates.or(PathSelectors.regex("/web.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("物联网云平台后台服务接口")//
                .version("1.0")//
                .build();
    }
}