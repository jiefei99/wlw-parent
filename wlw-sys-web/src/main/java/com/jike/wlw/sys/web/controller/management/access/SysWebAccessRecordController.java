package com.jike.wlw.sys.web.controller.management.access;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.management.access.AccessRecord;
import com.jike.wlw.service.management.access.AccessRecordFilter;
import com.jike.wlw.sys.web.config.fegin.AccessRecordFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "访问记录服务", tags = {"访问记录服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/access/record", produces = "application/json;charset=utf-8")
public class SysWebAccessRecordController extends BaseController {

    @Autowired
    private AccessRecordFeignClient accessRecordFeignClient;

    @ApiOperation(value = "根据查询条件查询访问记录")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<AccessRecord>> query(@ApiParam(required = true, value = "查询条件") @RequestBody AccessRecordFilter filter) throws BusinessException {
        try {
            PagingResult<AccessRecord> result = accessRecordFeignClient.query(filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
