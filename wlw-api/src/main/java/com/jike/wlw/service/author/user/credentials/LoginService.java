/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/29 23:55 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountForgetRq;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountLoginCredentialsRq;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountLoginIdModifyRq;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountModifyRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录服务
 */
@Api(tags = "登录服务")
@RequestMapping(value = "service/login", produces = "application/json;charset=utf-8")
public interface LoginService {

    @ApiOperation("用户密码登录")
    @RequestMapping(value = "/pwdLogin", method = RequestMethod.POST)
    @ResponseBody
    String pwdLogin(@ApiParam(required = true, value = "密码登录凭证") @RequestBody PwdAccountLoginCredentialsRq credentials) throws BusinessException;

    @ApiOperation("用户修改登录账号")
    @RequestMapping(value = "/modifyLoginId", method = RequestMethod.POST)
    @ResponseBody
    void modifyLoginId(@ApiParam(required = true, value = "修改登录账号请求参数") @RequestBody PwdAccountLoginIdModifyRq loginIdModifyRq) throws BusinessException;

    @ApiOperation("用户修改密码")
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ResponseBody
    void modifyPwd(@ApiParam(required = true, value = "修改密码请求参数") @RequestBody PwdAccountModifyRq modifyPwdRequest) throws BusinessException;

    @ApiOperation("用户忘记密码")
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    @ResponseBody
    void forgetPwd(@ApiParam(required = true, value = "修改密码请求参数") @RequestBody PwdAccountForgetRq forgetRq) throws BusinessException;

    @ApiOperation("用户重置密码")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    void reset(@ApiParam(required = true, value = "修改密码请求参数") @RequestBody PwdAccountModifyRq modifyPwdRequest) throws BusinessException;

}