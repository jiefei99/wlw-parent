package com.jike.wlw.service.management.access;

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

/**
 * 访问记录服务
 */
@Api(tags = "访问记录")
public interface AccessRecordService {

    @ApiOperation(value = "新增访问记录")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    void add(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
             @ApiParam(required = true, value = "查询条件") @RequestBody AccessRecord accessRecord) throws BusinessException;

    @ApiOperation(value = "查询访问记录")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<AccessRecord> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                     @ApiParam(required = true, value = "查询条件") @RequestBody AccessRecordFilter filter) throws BusinessException;

}
