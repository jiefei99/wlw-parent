package com.jike.wlw.sys.web.controller.author;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.permission.Permission;
import com.jike.wlw.service.author.user.permission.PermissionFilter;
import com.jike.wlw.service.author.user.role.RoleMenu;
import com.jike.wlw.service.author.user.role.UserRole;
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

@Api(value = "员工授权服务", tags = {"员工授权服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/auth", produces = "application/json;charset=utf-8")
public class SysWebAuthController extends BaseController {

    @Autowired
    private AuthFeignClient authFeignClient;
    @Autowired
    private EmployeeFeignClient employeeFeignClient;
    @Autowired
    private PermissionFeignClient permissionFeignClient;
    @Autowired
    private RoleMenuFeignClient roleMenuFeignClient;

    @ApiOperation(value = "保存用户角色")
    @RequestMapping(value = "/saveUserRoles", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> saveUserRoles(@ApiParam(required = true, value = "用户ID") @RequestParam("userId") String userId,
                                            @ApiParam(required = true, value = "角色ID集合Json字符串") @RequestParam("roleIdsJson") String roleIdsJson) throws BusinessException {
        try {
            authFeignClient.saveUserRoles(userId, roleIdsJson);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
    @ApiOperation(value = "通过用户列表保存用户角色")
    @RequestMapping(value = "/saveUsersRole", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> saveUsersRole(@ApiParam(required = true, value = "用户角色请求参数") @RequestBody UserRoleRq userRoleRq) throws BusinessException {
        try {
            authFeignClient.saveUsersRole(userRoleRq.getUserIdsJson(), userRoleRq.getRoleId());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "通过用户列表删除用户角色")
    @RequestMapping(value = "/removeUsersRole", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> removeUsersRole(@ApiParam(required = true, value = "用户角色请求参数") @RequestBody UserRoleRq userRoleRq) throws BusinessException {
        try {
            authFeignClient.removeUsersRole(userRoleRq.getUserIdsJson(), userRoleRq.getRoleId());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取用户角色")
    @RequestMapping(value = "/getUserRolesByUserId", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<List<UserRole>> getUserRolesByUserId(@ApiParam(value = "用户ID") @RequestParam(value = "userId", defaultValue = " ") String userId) throws BusinessException {
        try {
            List<UserRole> result = authFeignClient.getUserRolesByUserId(userId);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据条件查询用户角色")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<UserRole>> query(@ApiParam(required = true, value = "查询条件") @RequestBody AuthFilter filter) throws BusinessException {
        try {
            PagingResult<UserRole> result = authFeignClient.query(filter);

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

            List<UserRole> userRoleList = authFeignClient.getUserRolesByUserId(getUserId());

            PermissionFilter permissionFilter = new PermissionFilter();
            if (CollectionUtils.isEmpty(userRoleList)) {
                Employee employee = employeeFeignClient.get(getUserId());
                if (!employee.getAdmin()) {
                    return ActionResult.ok(result);
                }
            } else {
                List<String> roleIds = new ArrayList<>();
                for (UserRole userRole : userRoleList) {
                    roleIds.add(userRole.getRoleId());
                }

                AuthFilter authFilter = new AuthFilter();
                authFilter.setRoleIdIn(roleIds);
                List<RoleMenu> roleMenuList = roleMenuFeignClient.query(authFilter).getData();

                List<String> permissionIds = new ArrayList<>();
                for (RoleMenu roleMenu : roleMenuList) {
                    permissionIds.add(roleMenu.getPermissionId());
                }
                permissionFilter.setIdIn(permissionIds);
            }
            List<Permission> permissionList = permissionFeignClient.query(permissionFilter).getData();

            result = permissionList;

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
