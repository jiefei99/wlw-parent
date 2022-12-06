/**
 *
 */
package com.jike.wlw.sys.web.config.fegin;

import feign.Feign;
import feign.Request;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShadowX
 *
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignConfiguration {

//    @Autowired
//    private Environment env;
//
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthorizationInterceptor() {
//        String user = env.getProperty("shunlan-server.rest.username");
//        String password = env.getProperty("shunlan-server.rest.password");
//        return new BasicAuthRequestInterceptor(user, password);
//    }

    public static int connectTimeOutMillis = 20000;// 超时时间
    public static int readTimeOutMillis = 20000;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
    }
}
