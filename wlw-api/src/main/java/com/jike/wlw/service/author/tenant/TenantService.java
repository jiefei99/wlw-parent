/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 项目名：	yinlu-api
 * 文件名：	TenantService.java
 * 模块说明：
 * 修改历史：
 * 2020年3月10日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.tenant;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.jpa.api.entity.IdNameList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenpeisi
 */
public interface TenantService {

    @ApiOperation(value = "根据ID获取指定的租户")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Tenant get(@ApiParam(required = true, value = "ID") @RequestParam String id) throws BusinessException;

    @ApiOperation(value = "保存租户")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    String save(@ApiParam(required = true, value = "租户信息") @RequestBody Tenant tenant,
                @ApiParam(required = true, value = "操作人") @RequestParam String operator) throws BusinessException;

    @ApiOperation(value = "启用租户")
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    void enable(@ApiParam(required = true, value = "租户ID") @RequestParam String id,
                @ApiParam(required = true, value = "操作人") @RequestParam String operator) throws BusinessException;

    @ApiOperation(value = "停用租户")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    @ResponseBody
    void disable(@ApiParam(required = true, value = "租户ID") @RequestParam String id,
                 @ApiParam(required = true, value = "操作人") @RequestParam String operator) throws BusinessException;

    @ApiOperation(value = "根据条件查询租户")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Tenant> query(@ApiParam(required = true, value = "查询条件") @RequestBody TenantFilter filter) throws BusinessException;

    @ApiOperation(value = "fetch租户名称")
    @RequestMapping(value = "/fetchName", method = RequestMethod.POST)
    @ResponseBody
    void fetchName(@ApiParam(required = true, value = "ID集合") @RequestBody IdNameList idNameList) throws BusinessException;
}
