package com.jike.wlw.sys.web.controller.author.user.role;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.sys.web.config.fegin.RoleFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "角色服务", tags = {"角色服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/role", produces = "application/json;charset=utf-8")
public class SysWebRoleController extends BaseController {

    @Autowired
    private RoleFeignClient roleFeignClient;

    @ApiOperation(value = "根据角色ID获取指定的角色")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Role> get(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                  @ApiParam(required = true, value = "角色ID") @RequestParam(value = "id") String id) throws BusinessException {
        try {
            Role result = roleFeignClient.get(tenantId, id);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "保存角色")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> save(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                   @ApiParam(required = true, value = "新增角色请求参数") @RequestBody Role role) throws BusinessException {
        try {
            roleFeignClient.save(tenantId, role, getUserName());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改权限")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                     @ApiParam(required = true, value = "修改角色请求参数") @RequestBody Role role) throws BusinessException {
        try {
            roleFeignClient.modify(tenantId, role, getUserName());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据角色ID删除指定的角色")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> delete(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                     @ApiParam(required = true, value = "角色ID") @RequestParam(value = "id") String id) throws BusinessException {
        try {
            roleFeignClient.delete(tenantId, id);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询权限")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Role>> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                                  @ApiParam(required = true, value = "查询条件") @RequestBody RoleFilter filter) throws BusinessException {
        try {
            PagingResult<Role> result = roleFeignClient.query(tenantId, filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
