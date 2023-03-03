package com.jike.wlw.sys.web.controller.author.user;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.IdRequest;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserFilter;
import com.jike.wlw.service.author.user.UserModifyRq;
import com.jike.wlw.service.author.user.UserModifyStatusRq;
import com.jike.wlw.sys.web.config.fegin.UserFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.sso.AppContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "用户服务", tags = {"用户服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/user", produces = "application/json;charset=utf-8")
public class SysWebUserController extends BaseController {
    @Autowired
    private UserFeignClient userFeignClient;

    @ApiOperation(value = "获取当前用户信息")
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<User> getCurrentUser() throws Exception {
        try {
            User result = userFeignClient.get(getTenantId(), AppContext.getContext().getUserId());
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取当前用户信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<User> get(@ApiParam(required = true, value = "userId") @RequestParam(value = "userId") String userId) {
        try {
            User user = userFeignClient.get(getTenantId(), AppContext.getContext().getUserId());
            return ActionResult.ok(user);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改用户信息")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "修改用户请求参数") @RequestBody UserModifyRq modifyRq) throws BusinessException {
        try {
            userFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "账号冻结")
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> freeze(@ApiParam(required = true, value = "冻结账号ID") @RequestBody IdRequest idRequest) {
        try {

            User user = userFeignClient.get(getTenantId(), idRequest.getId());

            if (user == null) {
                throw new BusinessException("账号不存在或已删除");
            }
            UserModifyRq userModifyRq = new UserModifyRq();
            BeanUtils.copyProperties(user, userModifyRq);
            userModifyRq.setStatus(FreezeStatus.FREEZED);
            userFeignClient.modify(getTenantId(), userModifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "账号恢复")
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> unfreeze(@ApiParam(required = true, value = "恢复账号ID") @RequestBody IdRequest idRequest) {
        try {
            User user = userFeignClient.get(getTenantId(), idRequest.getId());
            if (user == null) {
                throw new BusinessException("账号不存在或已删除");
            }
            UserModifyRq modifyRq = new UserModifyRq();
            BeanUtils.copyProperties(user, modifyRq);
//            modifyRq.setUserId(idRequest.getId());
            modifyRq.setStatus(FreezeStatus.NORMAL);

            userFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }

    }

    @ApiOperation(value = "根据条件查询用户")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<User>> query(@ApiParam(required = true, value = "查询条件") @RequestBody UserFilter filter) throws BusinessException {
        try {
            PagingResult<User> result = userFeignClient.query(getTenantId(), filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "用户重置密码")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> reset() {
        try {
            //调用base的重置账号密码

//            loginFeignClient.reset(modifyRq);
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation("修改账号状态")
    @RequestMapping(value = "/modifyStatus", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> modifyStatus(@ApiParam("修改密码信息") @RequestBody UserModifyStatusRq modifyStatusRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyStatusRq, "modifyStatusRq");
            if (StringUtils.isBlank(modifyStatusRq.getUserId())){
                throw new BusinessException("用户id不能为空");
            }
            if (modifyStatusRq.getStatus()==null){
                throw new BusinessException("用户状态修改不能为空");
            }
            UserModifyRq modifyRq = new UserModifyRq();
            modifyRq.setUserId(modifyStatusRq.getUserId());
            modifyRq.setStatus(modifyStatusRq.getStatus());
            userFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


}
