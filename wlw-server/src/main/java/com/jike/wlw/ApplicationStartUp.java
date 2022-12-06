package com.jike.wlw;

import com.jike.wlw.service.author.user.employee.EmployeeCreateAdminRq;
import com.jike.wlw.service.author.user.employee.EmployeeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

@Slf4j
public class ApplicationStartUp implements ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;

    private Environment env;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
        env = applicationContext.getBean(Environment.class);
        log.info("应用程序启动初始化...");
        try {
            EmployeeCreateAdminRq admin = new EmployeeCreateAdminRq();
            admin.setName(env.getProperty("initialization.name"));
            admin.setMobile(env.getProperty("initialization.mobile"));
            admin.setLoginId(env.getProperty("initialization.loginId"));
            admin.setPassword(env.getProperty("initialization.password"));

            getEmployeeService().createAdmin(admin, "系统初始化");
            log.info("初始化管理员数据成功！");
        } catch (Exception e) {
            log.error("应用程序启动初始化失败。", e);
        }
    }

    private EmployeeService getEmployeeService() {
        return applicationContext.getBean(EmployeeService.class);
    }

}
