package com.jike.wlw.service.physicalmodel.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @title: PhysicalModelPropertyService
 * @Author RS
 * @Date: 2023/2/17 10:40
 * @Version 1.0
 */

@Api(tags = "私有物模型标准服务")
@RequestMapping(value = "service/physicalModelDataStandard", produces = "application/json;charset=utf-8")
public interface PhysicalModelDataStandardService {

    @ApiOperation(value = "新建物模型标准")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "添加物模型标准请求参数") @RequestParam PhysicalModelDataStandardCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "获取物模型标准")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    PhysicalModelDataStandard get(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                  @ApiParam(required = true, value = "父类Id") @RequestParam(value = "parentId") String parentId) throws BusinessException;

    @ApiOperation(value = "获取物模型标准集合")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    List<PhysicalModelDataStandard> query(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                          @ApiParam(required = true, value = "父类Id集合") @RequestParam(value = "parentIds") List<String> parentIds) throws BusinessException;

}


