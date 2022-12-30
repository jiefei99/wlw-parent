package com.jike.wlw.service.equipment;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "设备服务")
@RequestMapping(value = "service/equipment", produces = "application/json;charset=utf-8")
public interface EquipmentService {

    @ApiOperation(value = "根据ID获取设备")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Equipment get(@ApiParam(required = true, value = "ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新建设备")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "添加设备请求参数") @RequestBody EquipmentCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑设备")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "编辑设备请求参数") @RequestBody EquipmentModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据ID删除设备")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询设备")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Equipment> query(@ApiParam(required = true, value = "查询条件") @RequestBody EquipmentFilter filter) throws BusinessException;
}
