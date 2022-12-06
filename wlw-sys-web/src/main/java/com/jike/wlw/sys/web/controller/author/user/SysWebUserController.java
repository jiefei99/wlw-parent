package com.jike.wlw.sys.web.controller.author.user;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.IdRequest;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserFilter;
import com.jike.wlw.service.author.user.UserModifyRq;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountModifyRq;
import com.jike.wlw.sys.web.config.fegin.LoginFeignClient;
import com.jike.wlw.sys.web.config.fegin.UserFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.sso.AppContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private LoginFeignClient loginFeignClient;

    @ApiOperation(value = "获取当前用户信息")
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<User> getCurrentUser() throws Exception {
        try {
            User result = userFeignClient.get(AppContext.getContext().getUserId());
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
            User user = userFeignClient.get(AppContext.getContext().getUserId());
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
            userFeignClient.modify(modifyRq, AppContext.getContext().getUserName());
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

            User user = userFeignClient.get(idRequest.getId());

            if (user == null) {
                throw new BusinessException("账号不存在或已删除");
            }
            UserModifyRq userModifyRq = new UserModifyRq();
            BeanUtils.copyProperties(user, userModifyRq);
//            userModifyRq.setUserId(idRequest.getId());
            userModifyRq.setStatus(FreezeStatus.FREEZED);
            userFeignClient.modify(userModifyRq, AppContext.getContext().getUserName());
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
            User user = userFeignClient.get(idRequest.getId());
            if (user == null) {
                throw new BusinessException("账号不存在或已删除");
            }
            UserModifyRq modifyRq = new UserModifyRq();
            BeanUtils.copyProperties(user, modifyRq);
//            modifyRq.setUserId(idRequest.getId());
            modifyRq.setStatus(FreezeStatus.NORMAL);

            userFeignClient.modify(modifyRq, AppContext.getContext().getUserName());

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
            PagingResult<User> result = userFeignClient.query(filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "用户重置密码")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> reset(@ApiParam(required = true, value = "修改密码请求参数") @RequestBody PwdAccountModifyRq modifyRq) {
        try {
            loginFeignClient.reset(modifyRq);
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


}