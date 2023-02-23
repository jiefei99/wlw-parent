package com.jike.wlw.service.physicalmodel.ali;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.ModelVersion;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelCopyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPubTslGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishQueryRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelTsl;
import com.jike.wlw.service.physicalmodel.PhysicalModelTslGetRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Api(tags = "阿里物模型管理")
@RequestMapping(value = "service/physicalModeManager", produces = "application/json;charset=utf-8")
public interface PhysicalModelManagerService {

    @ApiOperation(value = "新建物模型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "添加物模型请求参数") @RequestBody PhysicalModelCreateRq createRq,
                  @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "指定产品物模型中的功能定义详情")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    PhysicalModel get(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                      @ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelGetRq modelGetRq) throws BusinessException;

    @ApiOperation(value = "获取指定产品的物模型版本")
    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    @ResponseBody
    List<ModelVersion> getVersion(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                  @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                                  @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "指定产品的已发布物模型TSL")
    @RequestMapping(value = "/getTslPublished", method = RequestMethod.POST)
    @ResponseBody
    PhysicalModelTsl getTslPublished(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelPubTslGetRq modelGetRq) throws BusinessException;

    @ApiOperation(value = "查询指定产品的物模型TSL")
    @RequestMapping(value = "/getTsl", method = RequestMethod.POST)
    @ResponseBody
    PhysicalModelTsl getTsl(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                            @ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelTslGetRq modelGetRq) throws BusinessException;

    @ApiOperation(value = "指定品类的物模型信息")
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    @ResponseBody
    PhysicalModel getCategory(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                     @ApiParam(required = false, value = "标识符") @RequestParam(value = "categoryKey") String categoryKey,
                     @ApiParam(required = false, value = "资源组Id") @RequestParam(value = "resourceGroupId") String resourceGroupId,
                     @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "编辑物模型")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "编辑请求参数") @RequestBody PhysicalModelModifyRq modifyRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "删除物模型")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "删除请求参数") @RequestBody PhysicalModelDelRq modelDelRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "获取物联网平台预定义的标准产品品类列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
//    PagingResult<Product>
    List<Map<String,String>> queryTemplates(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                      @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "查询指定产品已发布物模型中的功能定义详情")
    @RequestMapping(value = "/queryPublish", method = RequestMethod.POST)
    @ResponseBody
    PhysicalModel queryPublish(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                      @ApiParam(required = true, value = "请求参数") @RequestBody PhysicalModelPublishQueryRq filter) throws BusinessException;

    @ApiOperation(value = "复制物模型")
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    void copy(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
              @ApiParam(required = true, value = "复制请求参数") @RequestBody PhysicalModelCopyRq modifyRq,
              @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "发布物模型")
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    void publish(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
              @ApiParam(required = true, value = "复制请求参数") @RequestBody PhysicalModelPublishRq publishRq,
              @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;
}
