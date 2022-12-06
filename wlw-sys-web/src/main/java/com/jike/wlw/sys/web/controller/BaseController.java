/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/15 10:14 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web.controller;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 控制器基础服务
 */
@Slf4j
@ApiModel("控制器基础服务")
public class BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 获取SESSION
     */
    protected HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    /**
     * 获取请求
     */
    protected HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 获取用户ID
     */
    protected String getUserId() {
        return AppContext.getContext() == null ? null : AppContext.getContext().getUserId();
    }

    /**
     * 获取用户名
     */
    protected String getUserName() {
        return AppContext.getContext() == null ? null : AppContext.getContext().getUserName();
    }

    /**
     * IP
     */
    protected String getIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtil.isNullOrBlank(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isNullOrBlank(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtil.isNullOrBlank(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 获取回复
     */
    protected HttpServletResponse getResponse() {
        return response;
    }

    protected ActionResult dealWithError(Exception e) {
        log.error(e.getMessage());
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            return ActionResult.fail(businessException.getCode(),
                businessException.getFormatedMessage());
        } else {
            return ActionResult.fail(e.getMessage());
        }
    }

}