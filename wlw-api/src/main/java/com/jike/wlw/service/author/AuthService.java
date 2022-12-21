/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： tms-api
 * 文件名： AuthService.java
 * 模块说明：
 * 修改历史：
 * 2018年4月16日 - subinzhu - 创建。
 */
package com.jike.wlw.service.author;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.user.role.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 授权服务
 *
 * @author subinzhu
 */
@Api(tags = "授权服务")
public interface AuthService {

    @ApiOperation(value = "通过角色列表保存用户角色")
    @RequestMapping(value = "/saveUserRoles", method = RequestMethod.GET)
    @ResponseBody
    void saveUserRoles(@ApiParam(required = true, value = "用户ID") @RequestParam("userId") String userId,
                       @ApiParam(required = true, value = "角色ID集合Json字符串") @RequestParam("roleIdsJson") String roleIdsJson) throws BusinessException;

    @ApiOperation(value = "通过用户列表保存用户角色")
    @RequestMapping(value = "/saveUsersRole", method = RequestMethod.GET)
    @ResponseBody
    void saveUsersRole(@ApiParam(required = true, value = "用户ID集合字符串") @RequestParam("userIdsJson") String userIdsJson,
                       @ApiParam(required = true, value = "角色ID") @RequestParam("roleId") String roleId) throws BusinessException;

    @ApiOperation(value = "通过用户列表删除用户角色")
    @RequestMapping(value = "/removeUsersRole", method = RequestMethod.GET)
    @ResponseBody
    void removeUsersRole(@ApiParam(required = true, value = "用户ID集合字符串") @RequestParam("userIdsJson") String userIdsJson,
                       @ApiParam(required = true, value = "角色ID") @RequestParam("roleId") String roleId) throws BusinessException;

    @ApiOperation(value = "获取用户角色")
    @RequestMapping(value = "/getUserRolesByUserId", method = RequestMethod.GET)
    @ResponseBody
    List<UserRole> getUserRolesByUserId(@ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = " ") String userId) throws BusinessException;

    @ApiOperation(value = "根据条件查询用户角色")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<UserRole> query(@ApiParam(required = true, value = "查询条件") @RequestBody AuthFilter filter) throws BusinessException;

}
