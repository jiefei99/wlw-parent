/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	bigbomb-wechatweb
 * 文件名：	UserController.java
 * 模块说明：
 * 修改历史：
 * 2018年7月8日 - lsz - 创建。
 */
package com.jike.wlw.sys.web.controller.author.login;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.redis.RedisManager;
import com.geeker123.rumba.sso.TokenData;
import com.geeker123.rumba.sso.TokenService;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserType;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountLoginCredentialsRq;
import com.jike.wlw.service.management.access.AccessRecord;
import com.jike.wlw.service.management.access.AccessRecordFilter;
import com.jike.wlw.service.management.access.AccessRecordStatus;
import com.jike.wlw.sys.web.config.fegin.AccessRecordFeignClient;
import com.jike.wlw.sys.web.config.fegin.LoginFeignClient;
import com.jike.wlw.sys.web.config.fegin.UserFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.sso.AppConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

/**
 * @author lsz
 */

@Api(value = "登录服务", tags = {"登录服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/login", produces = "application/json;charset=utf-8")
public class SysWebLoginController extends BaseController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private LoginFeignClient loginFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private AccessRecordFeignClient accessRecordFeignClient;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> login(
            @ApiParam("登录信息") @RequestBody UserLoginCredentials credentials) {
        try {
            // 验证码检查
            if (getSession(false) == null) {
                return ActionResult.fail("验证码已失效，请刷新重试！");
            }
            String captcha = (String) getSession(false).getAttribute("captcha");
            if (captcha == null || credentials.getCaptcha() == null ||
                    captcha.toLowerCase().equals(credentials.getCaptcha().toLowerCase()) == false) {
                return ActionResult.fail("验证码不正确");
            }

            String userId;
            // 登录
            Integer times = 0;//登陆次数
            try {
                PwdAccountLoginCredentialsRq loginCredentials = credentials.buildUserCredentials();
                userId = loginFeignClient.pwdLogin(loginCredentials);
            } catch (Exception e) {
                if (e.getMessage().contains("账号密码错误")) {
                    String key =
                            getIp() + credentials.getLoginId();
                    times = redisManager.get(
                            key, Integer.class);
                    if (times != null) {
                        redisManager.setex(key, times + 1, 60 * 60 * 2);//两小时
                    } else {
                        times = 1;
                        redisManager.setex(key, 1, 60 * 60 * 24);
                    }
                }
                throw e;
            }

            // 发放token
            User user = userFeignClient.get(userId);
            if (user == null) {
                return ActionResult.fail("用户信息不存在或已被删除");
            }
            if (FreezeStatus.FREEZED == user.getStatus()) {
                return ActionResult.fail("您的账号已被冻结，请联系管理员");
            }

            // 缓存操作上下文信息
            TokenData tokenData = new TokenData();
            tokenData.put(AppConstant.APP_USER_TYPE, UserType.EMPLOYEE);
            tokenData.put(AppConstant.APP_USER_ID, user.getUuid());
            tokenData.put(AppConstant.APP_MOBILE, user.getMobile());
            tokenData.put(AppConstant.APP_USER_NAME, user.getName());

            String token = tokenService.issueToken(tokenData);
            tokenService.createCookie(getRequest(), getResponse(), token);

            // 记住账号
            Cookie cookie = new Cookie("userId", user.getUuid());
            cookie.setPath("/");
            cookie.setMaxAge(3 * 24 * 60 * 60);//半小时后失效
            getResponse().addCookie(cookie);

            //新IP登陆
            AccessRecordFilter recordFilter = new AccessRecordFilter();
            recordFilter.setIpEq(getIp());
            recordFilter.setStatusEq(AccessRecordStatus.ABNORMAL);
            recordFilter.setActionEq("新IP登陆");
            recordFilter.setPageSize(1);
            PagingResult<AccessRecord> accessRecords = accessRecordFeignClient.query(recordFilter);
            if (CollectionUtils.isEmpty(accessRecords.getData())) {
                addAccessRecord("新IP登陆", AccessRecordStatus.ABNORMAL);
                ActionResult result = new ActionResult();
                result.setMessage("当前登陆地址非常用地址，请确认账户安全");
                return result;
            } else {
                addAccessRecord("用户登陆", AccessRecordStatus.NORMAL);
            }

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "用户注销")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> logout() {
        try {
            tokenService.removeCookie(getRequest(), getResponse());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    private void addAccessRecord(String action, AccessRecordStatus status) {
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setIp(getIp());
        accessRecord.setStatus(status);
        accessRecord.setAction(action);
        accessRecord.setLoginUserId(getUserId());
        accessRecord.setLoginUserName(getUserName());
        accessRecordFeignClient.add(accessRecord);
    }

}

