package com.jike.wlw.service.source;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "资源信息服务")
@RequestMapping(value = "service/source", produces = "application/json;charset=utf-8")
public interface SourceService {

    @ApiOperation(value = "获取指定资源")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Source get(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
               @ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid) throws BusinessException;

    @ApiOperation(value = "新建资源")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "新建资源请求参数") @RequestBody SourceCreateRq createRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;


    @ApiOperation(value = "编辑资源")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid,
                @ApiParam(required = true, value = "编辑资源请求参数") @RequestBody SourceModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据ID删除资源")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "资源连接")
    @RequestMapping(value = "/connecting", method = RequestMethod.POST)
    @ResponseBody
    void connecting(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                    @ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid,
                    @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "资源断接")
    @RequestMapping(value = "/disConnect", method = RequestMethod.POST)
    @ResponseBody
    void disConnect(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                    @ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid,
                    @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "测试资源是否连接正常")
    @RequestMapping(value = "/checkConnecting", method = RequestMethod.POST)
    @ResponseBody
    void checkConnecting(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                         @ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid,
                         @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询资源")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Source> query(@ApiParam(required = true, value = "租户编号") @RequestParam(value = "tenantId") String tenantId,
                               @ApiParam(required = true, value = "查询条件") @RequestBody SourceFilter filter) throws BusinessException;
}
