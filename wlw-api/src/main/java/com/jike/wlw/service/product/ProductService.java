package com.jike.wlw.service.product;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "产品服务")
@RequestMapping(value = "service/product", produces = "application/json;charset=utf-8")
public interface ProductService {

    @ApiOperation(value = "根据ID获取产品")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Product get(@ApiParam(required = true, value = "ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "新建产品")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "添加产品请求参数") @RequestBody ProductCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑产品")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "编辑产品请求参数") @RequestBody ProductModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据ID删除产品")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "ID") @RequestParam(value = "id") String id) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询产品")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Product> query(@ApiParam(required = true, value = "查询条件") @RequestBody ProductFilter filter) throws BusinessException;

    @ApiOperation(value = "保存Influx")
    @RequestMapping(value = "/saveInflux", method = RequestMethod.POST)
    @ResponseBody
    void saveInflux(@ApiParam(required = true, value = "measurement") @RequestParam(value = "measurement") String measurement,
                    @ApiParam(required = true, value = "查询条件") @RequestBody Map<String, Object> fields,
                    @ApiParam(required = true, value = "查询条件") @RequestBody Map<String,String> tags) throws BusinessException;

    @ApiOperation(value = "查询Influx")
    @RequestMapping(value = "/queryInflux", method = RequestMethod.POST)
    @ResponseBody
    Object queryInflux(@ApiParam(required = true, value = "measurement") @RequestParam(value = "measurement") String measurement,
                       @ApiParam(required = true, value = "command") @RequestParam(value = "command") String command) throws BusinessException;

}
