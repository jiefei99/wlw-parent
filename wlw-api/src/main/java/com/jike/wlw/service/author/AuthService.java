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
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.author.user.role.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 授权服务
 *
 * @author subinzhu
 */
@Api(tags = "授权服务")
public interface AuthService {

    @ApiOperation(value = "保存角色")
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    @ResponseBody
    void saveRole(@ApiParam(value = "角色", required = true) @RequestBody Role role,
                  @ApiParam(value = "租户ID", required = true) @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(value = "操作者", required = true) @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/removeRole", method = RequestMethod.GET)
    @ResponseBody
    void removeRole(@ApiParam(value = "角色", required = true) @RequestParam(value = "roleId") String roleId,
                    @ApiParam(value = "租户ID", required = true) @RequestParam(value = "tenantId") String tenantId) throws BusinessException;

    @ApiOperation(value = "获取角色列表")
    @RequestMapping(value = "/queryRole", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Role> queryRole(@ApiParam(value = "查询条件", required = true) @RequestBody RoleFilter filter,
                                 @ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId) throws BusinessException;

    @ApiOperation(value = "通过用户列表保存用户角色")
    @RequestMapping(value = "/saveUsersRole", method = RequestMethod.GET)
    @ResponseBody
    void saveUsersRole(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                       @ApiParam(required = true, value = "用户ID集合字符串") @RequestParam("userIdsJson") String userIdsJson,
                       @ApiParam(required = true, value = "角色ID") @RequestParam("roleId") String roleId,
                       @ApiParam(required = true, value = "操作人用户ID") @RequestParam(value = "userId", required = false) String userId) throws BusinessException;

    @ApiOperation(value = "通过角色列表保存用户角色")
    @RequestMapping(value = "/saveUserRoles", method = RequestMethod.GET)
    @ResponseBody
    void saveUserRoles(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                       @ApiParam(required = true, value = "用户ID") @RequestParam("userId") String userId,
                       @ApiParam(required = true, value = "角色ID集合Json字符串") @RequestParam("roleIdsJson") String roleIdsJson) throws BusinessException;

    @ApiOperation(value = "通过用户列表删除用户角色")
    @RequestMapping(value = "/removeUserRole", method = RequestMethod.GET)
    @ResponseBody
    void removeUsersRole(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                         @ApiParam(required = true, value = "用户ID集合字符串") @RequestParam("userIdsJson") String userIdsJson,
                         @ApiParam(required = true, value = "角色ID") @RequestParam("roleId") String roleId,
                         @ApiParam(required = true, value = "操作人用户ID") @RequestParam(value = "userId", required = false) String userId) throws BusinessException;

    @ApiOperation(value = "通过角色列表删除用户")
    @RequestMapping(value = "/removeUserRoles", method = RequestMethod.GET)
    @ResponseBody
    void removeUserRoles(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                         @ApiParam(required = true, value = "用户ID") @RequestParam("userId") String userId) throws BusinessException;

    @ApiOperation(value = "获取用户的角色", response = List.class)
    @RequestMapping(value = "/getUserRoles", method = RequestMethod.GET)
    @ResponseBody
    List<UserRole> getUserRoles(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                @ApiParam(required = true, value = "用户ID") @RequestParam(value = "userId", defaultValue = " ") String userId) throws BusinessException;

    @ApiOperation(value = "根据条件查询用户角色")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<UserRole> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                 @ApiParam(required = true, value = "查询条件") @RequestBody AuthFilter filter) throws BusinessException;

}
