package com.jike.wlw.sys.web.controller.author.user.role;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.user.role.RoleMenu;
import com.jike.wlw.service.author.user.role.RoleMenuCreateRq;
import com.jike.wlw.sys.web.config.fegin.RoleMenuFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "角色权限菜单服务", tags = {"角色权限菜单服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/roleMenu", produces = "application/json;charset=utf-8")
public class SysWebRoleMenuController extends BaseController {

    @Autowired
    private RoleMenuFeignClient roleMenuFeignClient;

    @ApiOperation(value = "保存角色权限菜单")
    @RequestMapping(value = "/saveRoleMenus", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> saveRoleMenus(@ApiParam(required = true, value = "查询条件") @RequestBody RoleMenuCreateRq createRq) throws BusinessException {
        try {
            roleMenuFeignClient.saveRoleMenus(createRq);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据条件查询角色权限菜单")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<RoleMenu>> query(@ApiParam(required = true, value = "查询条件") @RequestBody AuthFilter filter) throws BusinessException {
        try {
            PagingResult<RoleMenu> result = roleMenuFeignClient.query(filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
