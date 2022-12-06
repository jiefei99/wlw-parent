package com.jike.wlw.service.author.org;

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
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Api(tags = "组织服务")
@RequestMapping(value = "service/org", produces = "application/json;charset=utf-8")
public interface OrgService {

    @ApiOperation(value = "通过用户ID获取指定组织")
    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    @ResponseBody
    Org get(@ApiParam(required = true , value = "用户ID") @RequestParam(value = "id") String id)throws BusinessException;

    @ApiOperation(value = "创建组织")
    @RequestMapping(value = "/create" , method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true , value = "新增组织请求参数") @RequestBody OrgCreateRq orgCreateRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator)throws BusinessException;

    @ApiOperation(value = "修改组织")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    String modify(@ApiParam(required = true, value = "修改组织请求参数") @RequestBody Org org,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询组织")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Org> query(@ApiParam(required = true, value = "查询条件") @RequestBody OrgFilter filter) throws BusinessException;
}
