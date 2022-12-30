package com.jike.wlw.service.physicalmodel.function;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "功能服务")
@RequestMapping(value = "service/function", produces = "application/json;charset=utf-8")
public interface FunctionService {

    @ApiOperation(value = "根据ID获取功能")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Function get(@ApiParam(required = true, value = "ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新建功能")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "添加功能请求参数") @RequestBody FunctionCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑功能")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "编辑功能请求参数") @RequestBody FunctionModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询功能")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Function> query(@ApiParam(required = true, value = "查询条件") @RequestBody FunctionFilter filter) throws BusinessException;
}
