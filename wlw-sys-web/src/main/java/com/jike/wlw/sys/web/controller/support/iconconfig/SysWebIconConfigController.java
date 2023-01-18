package com.jike.wlw.sys.web.controller.support.iconconfig;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.support.iconconfig.IconConfig;
import com.jike.wlw.service.support.iconconfig.IconConfigEditRq;
import com.jike.wlw.service.support.iconconfig.IconConfigFilter;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "图标配置服务", tags = {"图标配置服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/iconConfig", produces = "application/json;charset=utf-8")
public class SysWebIconConfigController extends BaseController {

    @Autowired
    private IconConfigFeignClient iconConfigFeignClient;

    @ApiOperation(value = "根据图标配置ID获取指定的图标配置")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<IconConfig> get(@ApiParam(required = true, value = "图标配置ID") @RequestParam(value = "id") String id) throws BusinessException {
        try {
            IconConfig result = iconConfigFeignClient.get(id);



            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新增图标配置")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "新增图标配置请求参数") @RequestBody IconConfigEditRq createRq) throws BusinessException {
        try {
            String result = iconConfigFeignClient.create(createRq, getUserName());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改图标配置")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "修改图标配置请求参数") @RequestBody IconConfigEditRq modifyRq) throws BusinessException {
        try {
            iconConfigFeignClient.modify(modifyRq, getUserName());

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据图标配置ID删除指定的图标配置")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> delete(@ApiParam(required = true, value = "图标配置ID") @RequestParam(value = "id") String id) throws BusinessException {
        try {
            iconConfigFeignClient.delete(id);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询图标配置")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<IconConfig>> query(@ApiParam(required = true, value = "查询条件") @RequestBody IconConfigFilter filter) throws BusinessException {
        try {
            PagingResult<IconConfig> result = iconConfigFeignClient.query(filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
