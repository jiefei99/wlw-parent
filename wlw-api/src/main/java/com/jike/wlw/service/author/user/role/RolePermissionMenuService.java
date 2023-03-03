/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： tms-api
 * 文件名： AuthService.java
 * 模块说明：
 * 修改历史：
 * 2018年4月16日 - subinzhu - 创建。
 */
package com.jike.wlw.service.author.user.role;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.auth.RolePermissionMenu;
import com.jike.wlw.service.author.auth.RolePermissionMenuCreateRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 授权服务
 *
 * @author subinzhu
 */
@Api(tags = "角色权限菜单")
public interface RolePermissionMenuService {

    @ApiOperation(value = "保存角色权限菜单")
    @RequestMapping(value = "/saveRolePermissionMenus", method = RequestMethod.POST)
    @ResponseBody
    void saveRolePermissionMenus(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                       @ApiParam(required = true, value = "查询条件") @RequestBody RolePermissionMenuCreateRq createRq) throws BusinessException;

    @ApiOperation(value = "根据条件查询角色权限菜单")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<RolePermissionMenu> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                           @ApiParam(required = true, value = "查询条件") @RequestBody AuthFilter filter) throws BusinessException;

}
