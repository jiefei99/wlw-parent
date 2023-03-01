/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	bigbomb-web
 * 文件名：	UserController.java
 * 模块说明：
 * 修改历史：
 * 2018年7月8日 - lsz - 创建。
 */
package com.jike.wlw.sys.web.controller.author.login;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.sso.TokenData;
import com.geeker123.rumba.sso.TokenService;
import com.jike.wlw.service.author.login.LoginCredentials;
import com.jike.wlw.service.author.user.UserType;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.sys.web.Constants;
import com.jike.wlw.sys.web.config.fegin.EmployeeFeignClient;
import com.jike.wlw.sys.web.config.fegin.LoginFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

/**
 * @author lsz
 */
@Slf4j
@Api(value = "登录服务", tags = {"web"})
@RestController
@RequestMapping(value = "/web/login", produces = "application/json;charset=utf-8")
public class WebLoginController extends BaseController {

    @Autowired
    private LoginFeignClient loginFeignClient;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmployeeFeignClient employeeFeignClient;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ActionResult<Void> login(@ApiParam("登录信息") @RequestBody WebUserLogin webLoginInfo) {
        try {
            // 验证码检查
            if (getSession(false) == null) {
                return ActionResult.fail("验证码已失效，请刷新重试！");
            }
            String captcha = (String) getSession(false).getAttribute("captcha");
            if (captcha == null || webLoginInfo.getCaptcha() == null || captcha.toLowerCase().equals(webLoginInfo.getCaptcha().toLowerCase()) == false) {
                return ActionResult.fail("验证码不正确");
            }

            // 登录
            LoginCredentials loginCredentials = webLoginInfo.buildUserCredentials();
            String userId = loginFeignClient.accountLogin(getTenantId(), loginCredentials);

            // 发放token
            Employee employee = employeeFeignClient.get(getTenantId(), userId);
            if (employee == null) {
                return ActionResult.fail("用户信息不存在");
            }

            TokenData tokenData = new TokenData();
            tokenData.put(Constants.Field.USER_TYPE, UserType.MEMBER.name());
//            tokenData.put(Constants.Field.USER_UUID, userId);
            tokenData.put(Constants.Field.USER, JsonUtil.objectToJson(employee));
            String token = tokenService.issueToken(tokenData);

            tokenService.createCookie(getRequest(), getResponse(), token);

            // 记住账号
            Cookie cookie = new Cookie("userId", employee.getUuid());
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60);
            getResponse().addCookie(cookie);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "用户注销")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public @ResponseBody
    ActionResult<Void> login() {
        try {
            tokenService.removeCookie(getRequest(), getResponse());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
