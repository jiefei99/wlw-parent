package com.jike.wlw.service.physicalmodel.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "私有物模型管理")
@RequestMapping(value = "service/physicalModeManagerPrivate", produces = "application/json;charset=utf-8")
public interface PhysicalModelManagerService {

    @ApiOperation(value = "新建物模型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "添加物模型请求参数") @RequestBody PhysicalModelCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "指定产品物模型中的功能定义详情")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    PhysicalModel get(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                      @ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelGetRq modelGetRq) throws BusinessException;

    @ApiOperation(value = "编辑物模型")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "编辑请求参数") @RequestBody PhysicalModelModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "删除物模型")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "删除请求参数") @RequestBody PhysicalModelDelRq modelDelRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

//    @ApiOperation(value = "发布物模型")
//    @RequestMapping(value = "/publish", method = RequestMethod.POST)
//    @ResponseBody
//    void publish(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
//                 @ApiParam(required = true, value = "复制请求参数") @RequestBody PhysicalModelPublishRq publishRq,
//                 @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

}
