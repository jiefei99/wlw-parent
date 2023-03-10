package com.jike.wlw.service.upgrade.ota.ali;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "阿里OTA升级-升级包")
public interface AliOTAUpgradePackageService {

    @ApiOperation(value = "获取OTA升级包列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OTAUpgradePackageVO> query(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId",required = false) String tenantId,
                                            @ApiParam(required = true, value = "查询请求参数") @RequestBody OTAUpgradePackageFilter filter) throws BusinessException;

    @ApiOperation(value = "保存OTA升级包")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId",required = false) String tenantId,
              @ApiParam(required = true, value = "创建请求参数") @RequestBody OTAUpgradePackageCreateRq createRq,
              @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator",required = false) String operator) throws BusinessException;

    @ApiOperation(value = "删除OTA升级包")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "删除请求参数") @RequestBody OTAUpgradePackageDeleteRq deleteRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator",required = false) String operator) throws BusinessException;

    @ApiOperation(value = "删除OTA升级包")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void getOTAUploadUrl( @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator",required = false) String operator) throws BusinessException;

}
