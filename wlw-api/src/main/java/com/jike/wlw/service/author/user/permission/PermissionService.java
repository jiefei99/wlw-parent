package com.jike.wlw.service.author.user.permission;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "权限服务")
public interface PermissionService {

    @ApiOperation(value = "根据权限ID获取指定的权限")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Permission get(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                   @ApiParam(required = true, value = "权限ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "保存权限")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    void save(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
              @ApiParam(required = true, value = "新增权限请求参数") @RequestBody PermissionSaveRq saveRq,
              @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改权限")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "修改权限请求参数") @RequestBody PermissionModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询权限")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Permission> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                   @ApiParam(required = true, value = "查询条件") @RequestBody PermissionFilter filter) throws BusinessException;

}
