package com.jike.wlw.service.support.iconconfig;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "图标配置服务")
@RequestMapping(value = "service/iconConfig", produces = "application/json;charset=utf-8")
public interface IconConfigService {

    @ApiOperation(value = "根据图标配置ID获取指定的图标配置")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    IconConfig get(@ApiParam(required = true, value = "图标配置ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新增图标配置")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "新增图标配置请求参数") @RequestBody IconConfigEditRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "修改图标配置")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "修改图标配置请求参数") @RequestBody IconConfigEditRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据图标配置ID删除指定的图标配置")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "图标配置ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询图标配置")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<IconConfig> query(@ApiParam(required = true, value = "查询条件") @RequestBody IconConfigFilter filter) throws BusinessException;
}
