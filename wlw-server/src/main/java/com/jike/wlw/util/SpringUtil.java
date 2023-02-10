package com.jike.wlw.util;

import com.alibaba.fastjson.JSON;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.core.support.emqx.EmqxClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author wza
 * @create 2023/1/12
 */

public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void set(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 通过字节码获取
     *
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * 通过BeanName获取
     *
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 通过beanName和字节码获取
     *
     * @param name
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> beanClass) {
        return context.getBean(name, beanClass);
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void replaceBean(String beanName, Object targetObj) throws NoSuchFieldException, IllegalAccessException {
        if (StringUtils.isBlank(beanName) || targetObj == null) {
            throw new BusinessException("无法替换bean，替换名称:" + beanName + ",替换对象:" + JSON.toJSONString(targetObj));
        }
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) getApplicationContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
//        if (beanFactory.containsBeanDefinition(beanName)) {
//        beanFactory.removeBeanDefinition(beanName);
//        }
//        if (beanFactory.containsBean(beanName)) {
//        beanFactory.destroySingleton(beanName);
//        }

        //反射获取Factory中的singletonObjects 将该名称下的bean进行替换
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.put(beanName, targetObj);
    }
}



