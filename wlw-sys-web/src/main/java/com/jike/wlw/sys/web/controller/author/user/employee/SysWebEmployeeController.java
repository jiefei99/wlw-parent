package com.jike.wlw.sys.web.controller.author.user.employee;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;

import com.jike.wlw.service.author.login.account.AccountUserModifyPwd;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.employee.EmployeeCreateRq;
import com.jike.wlw.service.author.user.employee.EmployeeFilter;
import com.jike.wlw.service.author.user.employee.EmployeeModifyRq;
import com.jike.wlw.sys.web.config.fegin.EmployeeFeignClient;
import com.jike.wlw.sys.web.config.fegin.LoginFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.sso.AppContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "员工服务", tags = {"员工服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/employee", produces = "application/json;charset=utf-8")
public class SysWebEmployeeController extends BaseController {

    @Autowired
    private EmployeeFeignClient employeeFeignClient;
    @Autowired
    private LoginFeignClient loginFeignClient;

    @ApiOperation(value = "获取当前登录用户")
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    public @ResponseBody
    ActionResult<Employee> getCurrentUser() throws Exception {
        try {
            return get(getUserId());
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取指定的员工信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Employee> get(@ApiParam(required = true, value = "userId") @RequestParam(value = "userId") String userId) throws Exception {
        try {
            Employee result = employeeFeignClient.get(getTenantId(), userId);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取指定的员工信息")
    @RequestMapping(value = "/getQueryEmployee", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Employee> getQueryEmployee() throws Exception {
        try {
            Employee result = employeeFeignClient.get(getTenantId(), getUserId());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "员工重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public @ResponseBody
    ActionResult<Void> resetPassword(@ApiParam("重置密码信息") @RequestBody String userId) {
        try {
            Assert.assertArgumentNotNull(userId, "userId");

            AccountUserModifyPwd modifyPwd = new AccountUserModifyPwd();
            modifyPwd.setNewPassword("123456");
            loginFeignClient.modifyPwd(getTenantId(),modifyPwd, getUserName());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新增员工")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "新增员工请求参数") @RequestBody EmployeeCreateRq createRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");
            String result = employeeFeignClient.create(getTenantId(), createRq, AppContext.getContext().getUserName());
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation("用户修改密码")
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> modifyPwd(@ApiParam("修改密码信息") @RequestBody AccountUserModifyPwd modifyPwd) throws BusinessException {
        try {
            modifyPwd.setUuid(StringUtils.isNotBlank(modifyPwd.getUserId())?modifyPwd.getUserId():getUserId());
            Employee currentUser = employeeFeignClient.get(getTenantId(),modifyPwd.getUserId());
            if (currentUser == null) {
                return ActionResult.fail("当前用户信息不存在或已删除");
            }
            loginFeignClient.modifyPwd(getTenantId(),modifyPwd, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "修改用户信息")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "修改用户请求参数") @RequestBody EmployeeModifyRq modifyRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");
            employeeFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询员工")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Employee>> query(@ApiParam(required = true, value = "查询条件") @RequestBody EmployeeFilter filter) throws BusinessException {
        try {
            if (filter.getAdminEq()){
                throw new BusinessException("此接口不适用于查询管理员");
            }
            filter.setAdminEq(false);
            PagingResult<Employee> result = employeeFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
