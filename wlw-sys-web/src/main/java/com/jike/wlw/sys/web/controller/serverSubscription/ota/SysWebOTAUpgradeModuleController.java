package com.jike.wlw.sys.web.controller.serverSubscription.ota;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleUpdateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradeModuleVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import com.jike.wlw.sys.web.config.fegin.AliOTAUpgradeModuleFeignClient;
import com.jike.wlw.sys.web.config.fegin.AliOTAUpgradePackageFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: SysWebOTAUpgradeModuleController
 * @Author RS
 * @Date: 2023/3/9 15:41
 * @Version 1.0
 */
@Slf4j
@Api(value = "OTA模块", tags = {"OTA升级"})
@RestController
@RequestMapping(value = "/web/OTAUpgradeModule", produces = "application/json;charset=utf-8")
public class SysWebOTAUpgradeModuleController extends BaseController {
    @Autowired
    private AliOTAUpgradeModuleFeignClient aliOTAUpgradeModuleFeignClient;

    @ApiOperation(value = "根据查询条件查询OTA升级模块")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradeModuleVO>> query(@RequestBody OTAUpgradeModuleFilter filter) throws BusinessException {
        try {
            if (filter.getPage()<0){
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize()<0||filter.getPageSize()>100){
                throw new BusinessException("每页显示数量0-100，请重新选择");
            }
            PagingResult<OTAUpgradeModuleVO> query = aliOTAUpgradeModuleFeignClient.query( filter);
            return ActionResult.ok(query);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建OTA升级模块")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> create(@RequestBody OTAUpgradeModuleCreateRq createRq) throws BusinessException {
        if (StringUtils.isBlank(createRq.getModuleName())){
            throw new BusinessException("当前OTA升级模块名称不能为空");
        }
        if (StringUtils.isBlank(createRq.getProductKey())){
            throw new BusinessException("productKey不能为空");
        }
        try {
            aliOTAUpgradeModuleFeignClient.create( createRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
    @ApiOperation(value = "修改OTA升级模块")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> update(@RequestBody OTAUpgradeModuleUpdateRq updateRq) throws BusinessException {
        if (StringUtils.isBlank(updateRq.getModuleName())){
            throw new BusinessException("当前OTA升级模块名称不能为空");
        }
        if (StringUtils.isBlank(updateRq.getProductKey())){
            throw new BusinessException("productKey不能为空");
        }
        try {
            aliOTAUpgradeModuleFeignClient.update( updateRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除OTA升级模块")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(@RequestBody OTAUpgradeModuleDeleteRq deleteRq) throws BusinessException {
        try {
            aliOTAUpgradeModuleFeignClient.delete(deleteRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}


