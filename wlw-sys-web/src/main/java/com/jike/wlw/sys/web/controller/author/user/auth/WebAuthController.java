package com.jike.wlw.sys.web.controller.author.user.auth;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.auth.RolePermissionMenu;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.permission.Permission;
import com.jike.wlw.service.author.user.permission.PermissionFilter;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.author.user.role.UserRole;
import com.jike.wlw.sys.web.config.fegin.AuthFeignClient;
import com.jike.wlw.sys.web.config.fegin.EmployeeFeignClient;
import com.jike.wlw.sys.web.config.fegin.PermissionFeignClient;
import com.jike.wlw.sys.web.config.fegin.RoleMenuFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(value = "授权管理", tags = {"web"})
@RestController
@RequestMapping(value = "/web/auth", produces = "application/json;charset=utf-8")
public class WebAuthController extends BaseController {

    @Autowired
    private AuthFeignClient authFeignClient;
    @Autowired
    private EmployeeFeignClient employeeFeignClient;
    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private RoleMenuFeignClient roleMenuFeignClient;

    @ApiOperation(value = "保存角色")
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> saveRole(@ApiParam(value = "角色", required = true) @RequestBody Role role) throws BusinessException {
        try {
            authFeignClient.saveRole(role, getTenantId());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/removeRole", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> removeRole(@ApiParam(value = "角色", required = true) @RequestParam(value = "roleId") String roleId) throws BusinessException {
        try {
            authFeignClient.removeRole(roleId, getTenantId());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取角色列表")
    @RequestMapping(value = "/queryRole", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Role>> queryRole(@ApiParam(value = "查询条件", required = true) @RequestBody RoleFilter filter) throws BusinessException {
        try {
            PagingResult<Role> result = authFeignClient.queryRole(filter, getTenantId());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "通过用户列表保存用户角色")
    @RequestMapping(value = "/saveUsersRole", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> saveUsersRole(@ApiParam(required = true, value = "用户ID集合字符串") @RequestParam("userIdsJson") String userIdsJson,
                                            @ApiParam(required = true, value = "角色ID") @RequestParam("roleId") String roleId) throws BusinessException {
        try {
            authFeignClient.saveUsersRole(getTenantId(), userIdsJson, roleId);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "通过角色列表保存用户角色")
    @RequestMapping(value = "/saveUserRoles", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> saveUserRoles(@ApiParam(required = true, value = "用户ID") @RequestParam("userId") String userId,
                                            @ApiParam(required = true, value = "角色ID集合Json字符串") @RequestParam("roleIdsJson") String roleIdsJson) throws BusinessException {
        try {
            authFeignClient.saveUserRoles(getTenantId(), userId, roleIdsJson);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "通过用户列表删除用户角色")
    @RequestMapping(value = "/removeUserRole", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> removeUsersRole(@ApiParam(required = true, value = "用户ID集合字符串") @RequestParam("userIdsJson") String userIdsJson,
                                              @ApiParam(required = true, value = "角色ID") @RequestParam("roleId") String roleId) throws BusinessException {
        try {
            authFeignClient.removeUsersRole(getTenantId(), userIdsJson, roleId);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "通过角色列表删除用户")
    @RequestMapping(value = "/removeUserRoles", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> removeUserRoles(@ApiParam(required = true, value = "用户ID") @RequestParam("userId") String userId) throws BusinessException {
        try {
            authFeignClient.removeUserRoles(getTenantId(), userId);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取用户的角色")
    @RequestMapping(value = "/getUserRoles", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<List<UserRole>> getUserRoles(@ApiParam(required = true, value = "用户ID") @RequestParam(value = "userId", defaultValue = " ") String userId) throws BusinessException {
        try {
            List<UserRole> result = authFeignClient.getUserRoles(getTenantId(), userId);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据条件查询角色的用户")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<UserRole>> query(@ApiParam(required = true, value = "查询条件") @RequestBody AuthFilter filter) throws BusinessException {
        try {
            PagingResult<UserRole> result = authFeignClient.query(getTenantId(), filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取用户权限")
    @RequestMapping(value = "/getUserPermission", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<List<Permission>> getUserPermission() throws BusinessException {
        try {
            List<Permission> result = new ArrayList<>();

            List<UserRole> userRoleList = authFeignClient.getUserRoles(getTenantId(), getUserId());

            PermissionFilter permissionFilter = new PermissionFilter();
            if (CollectionUtils.isEmpty(userRoleList)) {
                Employee employee = employeeFeignClient.get(getTenantId(), getUserId());
                if (employee.getAdmin()) {
                    result = permissionFeignClient.query(getTenantId(), permissionFilter).getData();
                    return ActionResult.ok(result);
                }
            } else {
                List<String> roleIds = new ArrayList<>();
                for (UserRole userRole : userRoleList) {
                    roleIds.add(userRole.getRoleId());
                }

                AuthFilter authFilter = new AuthFilter();
                authFilter.setRoleIdIn(roleIds);
                List<RolePermissionMenu> roleMenuList = roleMenuFeignClient.query(getTenantId(), authFilter).getData();

                List<String> permissionIds = new ArrayList<>();
                for (RolePermissionMenu roleMenu : roleMenuList) {
                    permissionIds.add(roleMenu.getPermissionId());
                }
                permissionFilter.setIdIn(permissionIds);
            }
            if (CollectionUtils.isEmpty(permissionFilter.getIdIn())) {
                return ActionResult.ok(result);
            }
            result = permissionFeignClient.query(getTenantId(), permissionFilter).getData();

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
