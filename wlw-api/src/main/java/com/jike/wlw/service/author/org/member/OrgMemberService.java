package com.jike.wlw.service.author.org.member;

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
import org.springframework.web.bind.annotation.RestController;


/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Api(tags = "组织成员服务")
@RestController
@RequestMapping(value = "service/org/member", produces = "application/json;charset=utf-8")
public interface OrgMemberService {

    @ApiOperation(value = "根据组织成员ID获取指定的组织成员")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    OrgMember get(@ApiParam(required = true, value = "组织成员ID") @RequestParam(value = "id") String id,
                  @ApiParam(required = false, value = "信息块") @RequestParam(required = false, value = "parts") String parts) throws BusinessException;

    @ApiOperation(value = "新增组织成员")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "新增组织成员请求参数") @RequestBody OrgMemberCreateRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改组织成员")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    String modify(@ApiParam(required = true, value = "新增/修改组织请求参数") @RequestBody OrgMember orgMember,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "设置管理员")
    @RequestMapping(value = "/set/admin", method = RequestMethod.GET)
    @ResponseBody
    void setAdmin(@ApiParam(required = true, value = "组织成员id") @RequestParam(value = "id") String id,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "启用")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    void enable(@ApiParam(required = true, value = "组织成员ID") @RequestParam(value = "id") String id,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "禁用")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    void disable(@ApiParam(required = true, value = "组织成员ID") @RequestParam(value = "id") String id,
                 @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "组织成员ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询组织成员")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OrgMember> query(@ApiParam(required = true, value = "查询条件") @RequestBody OrgMemberFilter filter) throws BusinessException;

}
