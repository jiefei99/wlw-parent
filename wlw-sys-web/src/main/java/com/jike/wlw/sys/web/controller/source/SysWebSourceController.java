package com.jike.wlw.sys.web.controller.source;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.source.Source;
import com.jike.wlw.service.source.SourceFilter;
import com.jike.wlw.service.source.SourceSaveRq;
import com.jike.wlw.service.source.SourceStatus;
import com.jike.wlw.sys.web.config.fegin.SourceFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.sso.AppContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "资源服务", tags = {"资源服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/source", produces = "application/json;charset=utf-8")
public class SysWebSourceController extends BaseController {

    @Autowired
    private SourceFeignClient sourceFeignClient;

    @ApiOperation(value = "获取指定的资源信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Source> get(@ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid) throws Exception {
        try {
            Source result = sourceFeignClient.get(getTenantId(), uuid);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新增资源信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "新增资源请求参数") @RequestBody SourceSaveRq createRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");
            String result = sourceFeignClient.create(getTenantId(), createRq, AppContext.getContext().getUserName());
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改资源信息")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid,
                                     @ApiParam(required = true, value = "修改资源请求参数") @RequestBody SourceSaveRq modifyRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(uuid, "uuid");
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");

            sourceFeignClient.modify(getTenantId(), uuid, modifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除指定的资源信息")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> delete(@ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid) throws Exception {
        try {
            Assert.assertArgumentNotNull(uuid, "uuid");
            sourceFeignClient.delete(getTenantId(), uuid, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "连接资源信息")
    @RequestMapping(value = "/connecting", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> connecting(@ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(uuid, "uuid");

            sourceFeignClient.connecting(getTenantId(), uuid, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "断开资源信息")
    @RequestMapping(value = "/disConnect", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> disConnect(@ApiParam(required = true, value = "uuid") @RequestParam(value = "uuid") String uuid) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(uuid, "uuid");

            sourceFeignClient.disConnect(getTenantId(), uuid, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询资源")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Source>> query(@ApiParam(required = true, value = "查询条件") @RequestBody SourceFilter filter) throws BusinessException {
        try {
            PagingResult<Source> result = sourceFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取当前租户已连接的资源")
    @RequestMapping(value = "/getConnectedSource", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Source> getConnectedSource() throws BusinessException {
        try {
            SourceFilter filter = new SourceFilter();
            filter.setStatusEq(SourceStatus.CONNECTED);
            filter.setDeletedEq(false);
            PagingResult<Source> pagingResult = sourceFeignClient.query(getTenantId(), filter);
            if (CollectionUtils.isEmpty(pagingResult.getData())) {
                return ActionResult.ok();
            }
            return ActionResult.ok(pagingResult.getData().get(0));
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
