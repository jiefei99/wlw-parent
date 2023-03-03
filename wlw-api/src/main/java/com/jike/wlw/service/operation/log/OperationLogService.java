package com.jike.wlw.service.operation.log;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/6
 * @apiNote
 */
@Api(tags = "操作日志服务")
public interface OperationLogService {

    @ApiOperation(value = "根据ID获取操作日志")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    OperationLog get(@ApiParam(value = "根据ID获取操作日志", required = true) @RequestParam(value = "id") String id,
                     @ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId) throws BusinessException;

    @ApiOperation(value = "创建操作日志")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(value = "创建操作日志", required = true) @RequestBody OperationLog operationLog,
                  @ApiParam(required = true) @RequestParam(value = "operator") String operator,
                  @ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId) throws BusinessException;

    @ApiOperation(value = "查询操作日志")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OperationLog> query(@ApiParam(value = "根据条件查询操作日志列表", required = true) @RequestBody OperationLogFilter filter,
                                     @ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId) throws BusinessException;

}
