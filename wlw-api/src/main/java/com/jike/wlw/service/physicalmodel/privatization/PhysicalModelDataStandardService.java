package com.jike.wlw.service.physicalmodel.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @title: PhysicalModelPropertyService
 * @Author RS
 * @Date: 2023/2/17 10:40
 * @Version 1.0
 */

@Api(tags = "私有物模型属性服务")
@RequestMapping(value = "service/physicalModelDataStandard", produces = "application/json;charset=utf-8")
public interface PhysicalModelDataStandardService {

    @ApiOperation(value = "新建物模型属性")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "添加物模型请求参数") @RequestBody PhysicalModelDataStandardCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

}


