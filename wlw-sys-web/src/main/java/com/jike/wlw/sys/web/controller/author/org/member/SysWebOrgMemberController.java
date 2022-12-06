package com.jike.wlw.sys.web.controller.author.org.member;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.org.member.*;
import com.jike.wlw.sys.web.config.fegin.OrgMemberFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mengchen
 * @date 2022/7/29
 * @apiNote
 */
@Api(value = "组织成员服务", tags = "组织成员服务")
@Slf4j
@RestController
@RequestMapping(value = "web/org/member", produces = "application/json;charset=utf-8")
public class SysWebOrgMemberController extends BaseController {
    @Autowired
    private OrgMemberFeignClient orgMemberFeignClient;

    @ApiOperation(value = "新增组织成员")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "新增组织成员请求参数") @RequestBody OrgMemberCreateRq createRq) throws BusinessException{
        try{
            String result = orgMemberFeignClient.create(createRq, getUserName());
            return ActionResult.ok(result);
        }catch (Exception e){
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改组织成员")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> modify(@ApiParam(required = true, value = "新增/修改组织请求参数") @RequestBody OrgMember orgMember) throws BusinessException{
        try{
            String result = orgMemberFeignClient.modify(orgMember, getUserName());
            return ActionResult.ok(result);
        }catch (Exception e){
            return dealWithError(e);
        }
    }
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> delete(@ApiParam(required = true, value = "组织成员ID") @RequestParam(value = "id") String id) throws BusinessException{
        try{
            orgMemberFeignClient.delete(id);
            return ActionResult.ok();
        }catch (Exception e){
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "设置管理员")
    @RequestMapping(value = "/set/admin", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> setAdmin(@ApiParam(required = true, value = "组织成员id") @RequestParam(value = "id") String id) throws BusinessException{
        try {
            orgMemberFeignClient.setAdmin(id,getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据条件查询当前组织所有成员")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OrgMember>> query(@ApiParam(required = true, value = "查询条件") @RequestBody OrgMemberFilter filter) throws BusinessException{
        try{
            PagingResult<OrgMember> result = orgMemberFeignClient.query(filter);
            return ActionResult.ok(result);
        }catch (Exception e){
            return dealWithError(e);
        }
    }
}
