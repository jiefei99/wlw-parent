/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/23 13:19 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user;

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

@Api(tags = "用户服务")
public interface UserService {

    @ApiOperation(value = "根据用户ID获取指定的用户")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    User get(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
             @ApiParam(required = true, value = "用户ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新增用户")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "新增用户请求参数") @RequestBody UserCreateRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改用户")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "修改用户请求参数") @RequestBody UserModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询用户")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<User> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                             @ApiParam(required = true, value = "查询条件") @RequestBody UserFilter filter) throws BusinessException;


}