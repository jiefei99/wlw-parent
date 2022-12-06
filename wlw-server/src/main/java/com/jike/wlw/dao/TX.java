/**
 *
 */
package com.jike.wlw.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ShadowX
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Transactional(value = "wlw-server.txManager", propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public @interface TX {

}
