package com.jike.wlw.sys.web.controller.author.user.permission;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.user.permission.Permission;
import com.jike.wlw.service.author.user.permission.PermissionFilter;
import com.jike.wlw.service.author.user.permission.PermissionModifyRq;
import com.jike.wlw.service.author.user.permission.PermissionSaveRq;
import com.jike.wlw.sys.web.config.fegin.PermissionFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "权限服务", tags = {"权限服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/permission", produces = "application/json;charset=utf-8")
public class SysWebPermissionController extends BaseController {

    @Autowired
    private PermissionFeignClient permissionFeignClient;

    @ApiOperation(value = "根据权限ID获取指定的权限")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Permission> get(@ApiParam(required = true, value = "权限ID") @RequestParam(value = "id") String id) throws BusinessException {
        try {
            Permission result = permissionFeignClient.get(id);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "保存权限")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> save(@ApiParam(required = true, value = "新增权限请求参数") @RequestBody PermissionSaveRq saveRq) throws BusinessException {
        try {
            permissionFeignClient.save(saveRq, getUserName());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询权限")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Permission>> query(@ApiParam(required = true, value = "查询条件") @RequestBody PermissionFilter filter) throws BusinessException {
        try {
            PagingResult<Permission> result = permissionFeignClient.query(filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
