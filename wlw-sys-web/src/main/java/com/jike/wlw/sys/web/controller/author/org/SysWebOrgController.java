package com.jike.wlw.sys.web.controller.author.org;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.author.org.Org;
import com.jike.wlw.service.author.org.OrgCreateRq;
import com.jike.wlw.service.author.org.OrgFilter;
import com.jike.wlw.sys.web.config.fegin.OrgFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author mengchen
 * @date 2022/7/21
 * @apiNote
 */
@Api(value = "组织服务", tags = {"组织服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/org",produces = "application/json;charset = utf-8")
public class SysWebOrgController extends BaseController {
    @Autowired
    private OrgFeignClient orgFeignClient;

//    @Autowired
//    private RemoteGoodsService remoteGoodsService;

    @ApiOperation(value = "根据ID获取组织")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Org> get(@ApiParam(required = true, value = "用户ID") @RequestParam("id") String id) throws BusinessException {
        try {
            Org result = orgFeignClient.get(id);
//            Object result = remoteGoodsService.getById("1003");

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新增组织")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> save(@ApiParam(required = true, value = "新增组织请求参数") @RequestBody OrgCreateRq orgCreateRq) throws BusinessException {
        try {
            String result = orgFeignClient.create(orgCreateRq, getUserName());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改组织")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "修改组织请求参数") @RequestBody Org org) throws BusinessException {
        try {
            String result = orgFeignClient.modify(org, getUserName());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询组织")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Org>> query(@ApiParam(required = true, value = "查询条件") @RequestBody OrgFilter filter) throws BusinessException {
        try {
            PagingResult<Org> result = orgFeignClient.query(filter);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


}
