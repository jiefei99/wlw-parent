package com.jike.wlw.service.product.info.ali;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.product.info.BaseProductService;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.ProductModifyRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "阿里云产品服务")
public interface AliProductService extends BaseProductService {

    @ApiOperation(value = "根据ID获取产品")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    Product get(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "新建产品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "添加产品请求参数") @RequestBody ProductCreateRq createRq,
                  @ApiParam(required = false, value = "操作人") @RequestParam(required = false, value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑产品")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "编辑产品请求参数") @RequestBody ProductModifyRq modifyRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(required = false, value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据ID删除产品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId,
                @ApiParam(required = false, value = "操作人") @RequestParam(required = false, value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询产品")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Product> query(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                @ApiParam(required = true, value = "查询产品请求参数") @RequestBody ProductFilter filter) throws BusinessException;

    @ApiOperation(value = "发布指定产品")
    @RequestMapping(value = "/releaseProduct", method = RequestMethod.POST)
    @ResponseBody
    void publishProduct(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                        @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                        @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId,
                        @ApiParam(required = false, value = "操作人") @RequestParam(required = false, value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "取消指定产品的发布")
    @RequestMapping(value = "/cancelReleaseProduct", method = RequestMethod.POST)
    @ResponseBody
    void unPublishProduct(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                          @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                          @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId,
                          @ApiParam(required = false, value = "操作人") @RequestParam(required = false, value = "operator") String operator) throws BusinessException;

}
