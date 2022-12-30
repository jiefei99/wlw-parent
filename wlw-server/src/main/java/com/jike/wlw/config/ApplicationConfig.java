/**
 *
 */
package com.jike.wlw.config;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.geeker123.rumba.commons.redis.RedisManager;
import com.geeker123.rumba.commons.util.AppCtxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.CharacterEncodingFilter;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ShadowX
 *
 */
@Configuration
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(smt);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter(
            @Autowired ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public RedisManager getRedisManager() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(env.getProperty("redis.maxIdle")));
        config.setMaxTotal(Integer.valueOf(env.getProperty("redis.maxTotal")));
        config.setMaxWaitMillis(Integer.valueOf(env.getProperty("redis.maxWaitMillis")));
        RedisManager target = new RedisManager(env.getProperty("redis.address"), env.getProperty("redis.password"), config);
        return target;
    }

    @Bean
    public StringHttpMessageConverter getStringHttpMessageConverter() {
        return new StringHttpMessageConverter();
    }

    @Bean
    public RestTemplate getRestTemplate(ObjectMapper objectMapper) {
        RestTemplate template = new RestTemplate();
        template.setMessageConverters(
                Arrays.asList(new StringHttpMessageConverter(Charset.forName("UTF-8")),
                        new MappingJackson2HttpMessageConverter(objectMapper)));
        return template;
    }

    /**
     * 操作上下文
     **/
    @Bean
    public AppCtxUtil getAppContext() {
        return AppCtxUtil.getInstance();
    }

    /**
     * Spring 字符集编码过滤器
     **/
    @Bean
    public FilterRegistrationBean registerEncodingFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        // 注入过滤器
        filter.setFilter(new CharacterEncodingFilter());
        // 过滤器名称
        filter.setName("EncodingFilter");
        // 初始化参数
        filter.addInitParameter("encoding", "UTF-8");
        filter.addInitParameter("forceEncoding", "true");
        // 拦截规则
        filter.addUrlPatterns("/*");
        return filter;
    }


}
