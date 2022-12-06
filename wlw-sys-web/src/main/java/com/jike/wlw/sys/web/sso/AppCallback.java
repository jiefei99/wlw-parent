/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/15 10:26 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web.sso;

import com.geeker123.rumba.sso.TokenCallback;
import com.geeker123.rumba.sso.TokenData;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 应用SSO回调
 */
@Slf4j
@ApiModel("应用SSO回调")
public class AppCallback implements TokenCallback {

    @Override
    public void onAccepted(HttpServletRequest req, HttpServletResponse resp, TokenData tokenData) {
        String userType = (String) tokenData.get(AppConstant.APP_USER_TYPE);
        String userId = (String) tokenData.get(AppConstant.APP_USER_ID);
        String userName = (String) tokenData.get(AppConstant.APP_USER_NAME);
        String mobile = (String) tokenData.get(AppConstant.APP_MOBILE);

        AppContext context = AppContext.getContext();
        if (context == null) {
            context = new AppContext();
        }

        context.setUserType(userType);
        context.setUserId(userId);
        context.setUserName(userName);
        context.setMobile(mobile);
        AppContext.setContext(context);
    }

    @Override
    public void onRejected(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        log.info("注销: {}", session != null ? session.getId() : null);
    }

    @Override
    public boolean onValidate(HttpServletRequest req, HttpServletResponse resp) {
        return true;
    }
}