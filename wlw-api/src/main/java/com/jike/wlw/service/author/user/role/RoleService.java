package com.jike.wlw.service.author.user.role;

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

@Api(tags = "角色服务")
@RequestMapping(value = "service/role", produces = "application/json;charset=utf-8")
public interface RoleService {

    @ApiOperation(value = "根据角色ID获取指定的角色")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Role get(@ApiParam(required = true, value = "角色ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "保存角色")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    void save(@ApiParam(required = true, value = "新增角色请求参数") @RequestBody Role role,
              @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改角色")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "修改角色请求参数") @RequestBody Role role,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据角色ID删除指定的角色")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "角色ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询权限")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Role> query(@ApiParam(required = true, value = "查询条件") @RequestBody RoleFilter filter) throws BusinessException;
}
