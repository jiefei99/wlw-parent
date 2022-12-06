/**
 * 
 */
package com.jike.wlw.config.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * @author ShadowX
 *
 */
@Configuration
public class JdbcConfig {

  @Bean(name = "wlw-server.txManager")
  public DataSourceTransactionManager transactionManager(DataSource ds) {
    return new DataSourceTransactionManager(ds);
  }

  @Bean("wlw-server.jdbcTemplate")
  public JdbcTemplate jdbcTemplate(DataSource ds) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
    return jdbcTemplate;
  }

  @Bean
  public ServletRegistrationBean druidStatViewServlet() {
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
        new StatViewServlet(), "/druid/*");
    servletRegistrationBean.addInitParameter("allow", "");
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "QojgxwugTQKv8dsxKUEG");
    return servletRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean druidStatFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
    filterRegistrationBean.addInitParameter("sessionStatEnable", "false");
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.addInitParameter("exclusions",
        "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    return filterRegistrationBean;
  }

}
