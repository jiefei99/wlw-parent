package com.jike.wlw.service.physicalmodel.privatization;


import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionDelRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "私有物模型功能服务")
@RequestMapping(value = "service/physicalModelFunction", produces = "application/json;charset=utf-8")
public interface PhysicalModelFunctionService {
    @ApiOperation(value = "新建物模型功能")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "添加物模型请求参数") @RequestBody PhysicalModelFunctionCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;


    @ApiOperation(value = "删除物模型")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "删除请求参数") @RequestBody PhysicalModelFunctionDelRq delRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

}
