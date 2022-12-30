package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "物模型服务")
@RequestMapping(value = "service/physicalModel", produces = "application/json;charset=utf-8")
public interface PhysicalModelService {

    @ApiOperation(value = "根据ID获取物模型")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    PhysicalModel get(@ApiParam(required = true, value = "ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新建物模型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "添加物模型请求参数") @RequestBody PhysicalModel createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑物模型")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "编辑物模型请求参数") @RequestBody PhysicalModel modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询物模型")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<PhysicalModel> query(@ApiParam(required = true, value = "查询条件") @RequestBody PhysicalModelFilter filter) throws BusinessException;
}
