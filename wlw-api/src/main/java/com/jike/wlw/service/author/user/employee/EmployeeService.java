/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/23 13:20 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.employee;

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

/**
 * 员工服务
 */
@Api(tags = "员工服务")
public interface EmployeeService {

    @ApiOperation(value = "根据用户ID获取指定的员工")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Employee get(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                 @ApiParam(required = true, value = "用户id") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新增员工")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "新增员工请求参数") @RequestBody EmployeeCreateRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "新增管理员")
    @RequestMapping(value = "/createAdmin", method = RequestMethod.POST)
    @ResponseBody
    String createAdmin(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                       @ApiParam(required = true, value = "新增管理员请求参数") @RequestBody EmployeeCreateAdminRq adminRq,
                       @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改员工")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "修改用户请求参数") @RequestBody EmployeeModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改员工状态")
    @RequestMapping(value = "/modifyStatus", method = RequestMethod.POST)
    @ResponseBody
    void modifyStatus(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "修改用户请求参数") @RequestBody EmployeeModifyStatusRq modifyStatusRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询所有员工")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Employee> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                 @ApiParam(required = true, value = "查询条件") @RequestBody EmployeeFilter filter) throws BusinessException;


    @ApiOperation(value = "根据登录ID获取指定的员工")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Employee getEmployeeByUserId(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                 @ApiParam(required = true, value = "登录id") @RequestParam(value = "userId") String userId) throws BusinessException;

}
