package com.jike.wlw.sys.web.controller.operation.log;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.operation.log.OperationLog;
import com.jike.wlw.service.operation.log.OperationLogFilter;
import com.jike.wlw.sys.web.config.fegin.OperationLogFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/24 10:17- zhengzhoudong - 创建。
 */
@Api(value = "操作日志服务", tags = {"操作日志服务"})
@RestController
@RequestMapping(value = "/web/operation/log", produces = "application/json;charset=utf-8")
public class SysWebOperationLogController extends BaseController {

    @Autowired
    private OperationLogFeignClient operationLogFeignClient;

    @ApiOperation(value = "查询操作日志")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<PagingResult<OperationLog>> query(@ApiParam(value = "操作日志查询条件", required = true) @RequestBody OperationLogFilter filter) throws BusinessException {
        try {
            PagingResult<OperationLog> result = operationLogFeignClient.query(filter, getTenantId());
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
