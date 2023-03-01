/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	mark-web
 * 文件名：	LoginService.java
 * 模块说明：
 * 修改历史：
 * 2019年6月12日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.login;


import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.author.login.account.AccountUserModifyPwd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zrs
 */

@Api(tags = "登录服务")
@RequestMapping(value = "/service/login", produces = "application/json;charset=utf-8")
public interface LoginService {

    @ApiOperation(value = "账号密码登录")
    @RequestMapping(value = "/accountLogin", method = RequestMethod.POST)
    @ResponseBody
    String accountLogin(@ApiParam(value = "登录凭证", required = true) @RequestBody LoginCredentials credentials) throws BusinessException;


    @ApiOperation(value = "修改账号密码")
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ResponseBody
    void modifyPwd(@ApiParam(value = "修改密码请求参数", required = true) @RequestBody AccountUserModifyPwd modifyPwd,
                   @ApiParam(value = "操作人", required = true) @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "冻结账号")
    @RequestMapping(value = "/freeze", method = RequestMethod.GET)
    @ResponseBody
    void freeze(@ApiParam(value = "账号ID", required = true) @RequestParam(value = "id") String id,
                @ApiParam(value = "操作人", required = true) @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "解冻账号")
    @RequestMapping(value = "/unfreeze", method = RequestMethod.GET)
    @ResponseBody
    void unfreeze(@ApiParam(value = "账号ID", required = true) @RequestParam(value = "uuid") String uuid,
                  @ApiParam(value = "操作人", required = true) @RequestParam(value = "operator") String operator) throws BusinessException;
}
