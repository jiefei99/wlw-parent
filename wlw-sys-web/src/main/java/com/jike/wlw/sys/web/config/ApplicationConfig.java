/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	mark-wechatweb
 * 文件名：	ApplicationCondig.java
 * 模块说明：
 * 修改历史：
 * 2018年9月27日 - lsz - 创建。
 */
package com.jike.wlw.sys.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geeker123.rumba.commons.redis.RedisManager;
import com.geeker123.rumba.commons.util.AppCtxUtil;
import com.geeker123.rumba.sso.LoginConfig;
import com.geeker123.rumba.sso.TokenConfig;
import com.geeker123.rumba.sso.TokenService;
import com.geeker123.rumba.sso.WebTokenFilter;
import com.geeker123.rumba.web.base.servlet.BarcodeServlet;
import com.geeker123.rumba.web.base.servlet.CaptchaServlet;
import com.jike.wlw.sys.web.sso.AppCallback;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * @author lsz
 */
@Configuration
@ConditionalOnClass({ Feign.class})
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

    // ========================================Filter========================================//

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

    /**
     * sysweb token 过滤器
     **/
    @Bean
    public FilterRegistrationBean registerTokenFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        // 注入过滤器
        filter.setFilter(new WebTokenFilter());
        // 过滤器名称
        filter.setName("WebTokenFilter");
        // 初始化参数
        filter.addInitParameter("excludes",
                "*/org/*,*/order/getProductionOrder,*/login/*,*/wall/*,*.css,*.js,*.png,*.jpg*,*swagger-ui.html*,*/webjars*,*/v2/api-docs*,*/swagger-resources*,*/jike/shutdown");
        // 拦截规则
        filter.addUrlPatterns("/web/*");
        return filter;
    }

    // ========================================Servlet========================================//

    /**
     * 验证码服务
     **/
    @Bean
    public ServletRegistrationBean registerCaptchaServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean();
        // 注入服务
        servlet.setServlet(new CaptchaServlet());
        // 服务名称
        servlet.setName("CaptchaServlet");
        // 拦截规则
        servlet.addUrlMappings("/web/captcha.jpg");
        return servlet;
    }

    /**
     * 二维码服务
     **/
    @Bean
    public ServletRegistrationBean registerBarcodeServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean();
        // 注入服务
        servlet.setServlet(new BarcodeServlet());
        // 服务名称
        servlet.setName("BarcodeServlet");
        // 拦截规则
        servlet.addUrlMappings("/web/barcode.jpg");
        return servlet;
    }

    // ========================================其它========================================//

    /**
     * 操作上下文
     **/
    @Bean
    public AppCtxUtil getAppContext() {
        return AppCtxUtil.getInstance();
    }

    /**
     * 登录配置
     **/
    @Bean
    public LoginConfig getLoginConfig() {
        LoginConfig target = new LoginConfig();
        target.setLoginUrl(env.getProperty("login.url"));
        target.setLogoutUrl(env.getProperty("logout.url"));
        return target;
    }

    /**
     * Token配置
     **/
    @Bean
    public TokenService getTokenService() {
        TokenService target = new TokenService();
        TokenConfig config = new TokenConfig();
        config.setExpiresIn(Integer.valueOf(env.getProperty("login.expiresIn")));
        config.setRefreshIn(Integer.valueOf(env.getProperty("login.refreshIn")));
        config.setSecret(env.getProperty("login.secret"));
        target.setConfig(config);
        return target;
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

    /**
     * SSO回调
     **/
    @Bean
    public AppCallback getAppCallback() {
        return new AppCallback();
    }

    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignRequestMappingHandlerMapping();
            }
        };
    }

    /**
     * swagger过滤同时被 @FeignClient和@RequestMapping 修饰的类
     */
    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) &&
                !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
        }
    }
}
