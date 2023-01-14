/**
 *
 */
package com.jike.wlw.sys.web.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author ShadowX
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String username = env.getProperty("wlw-sys-web.rest.username");
        String password = env.getProperty("wlw-sys-web.rest.password");
        auth.inMemoryAuthentication().withUser(username).password(password).roles("USER");

        // 添加停止服务的认证用户
        username = env.getProperty("endpoints.shutdown.security.user.name");
        password = env.getProperty("endpoints.shutdown.security.user.password");
        auth.inMemoryAuthentication().withUser(username).password(password).roles("ACTUATOR");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/service/**").hasRole("USER")
                .antMatchers("*/jike/shutdown").hasRole("ACTUATOR").and().sessionManagement().and().logout()
                .and().httpBasic();
    }
}