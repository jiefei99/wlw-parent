package com.jike.wlw.sys.web.controller.serverSubscription.ota;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import com.jike.wlw.sys.web.config.fegin.AliOTAUpgradePackageFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: SysWebOTAUpgradeController
 * @Author RS
 * @Date: 2023/3/8 16:43
 * @Version 1.0
 */
@Slf4j
@Api(value = "OTA升级包", tags = {"OTA升级"})
@RestController
@RequestMapping(value = "/web/OTAUpgradePackage", produces = "application/json;charset=utf-8")
public class SysWebOTAUpgradePackageController extends BaseController {

    @Autowired
    private AliOTAUpgradePackageFeignClient aliOTAUpgradePackageFeignClient;

    @ApiOperation(value = "根据查询条件查询OTA升级包")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageListVO>> query(@RequestBody OTAUpgradePackageFilter filter) throws BusinessException {
        try {
            if (filter.getPage()<0){
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize()<0||filter.getPageSize()>100){
                throw new BusinessException("每页显示数量0-100，请重新选择");
            }
            PagingResult<OTAUpgradePackageListVO> query = aliOTAUpgradePackageFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(query);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
    @ApiOperation(value = "根据查询条件查询OTA升级包")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageListVO>> get(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                                   @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)){
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageVO otaUpgradePackageVO = aliOTAUpgradePackageFeignClient.get(getTenantId(), id, iotInstanceId);
            return ActionResult.ok(otaUpgradePackageVO);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建OTA升级包")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> create(@RequestBody OTAUpgradePackageCreateRq createRq) throws BusinessException {
        if (StringUtils.isBlank(createRq.getDestVersion())){
            throw new BusinessException("当前OTA升级包版本号不能为空");
        }
        if (StringUtils.isBlank(createRq.getFirmwareName())){
            throw new BusinessException("升级包名称不能为空");
        }
        try {
            aliOTAUpgradePackageFeignClient.create(getTenantId(), createRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除OTA升级包")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(@RequestBody OTAUpgradePackageDeleteRq deleteRq) throws BusinessException {
        try {
            aliOTAUpgradePackageFeignClient.delete(deleteRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

//    public ActionResult<Void> getOTAUploadUrl(){
//        try {
//            aliOTAUpgradePackageFeignClient.getOTAUploadUrl(getUserName());
//            return ActionResult.ok();
//        } catch (Exception e) {
//            return dealWithError(e);
//        }
//    }
}


