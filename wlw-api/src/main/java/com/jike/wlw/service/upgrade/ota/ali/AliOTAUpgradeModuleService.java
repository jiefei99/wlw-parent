package com.jike.wlw.service.upgrade.ota.ali;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleUpdateRq;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradeModuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "阿里OTA升级-模块")
public interface AliOTAUpgradeModuleService {

    @ApiOperation(value = "获取OTA升级模块列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OTAUpgradeModuleVO> query(@ApiParam(required = true, value = "查询请求参数") @RequestBody OTAUpgradeModuleFilter filter) throws BusinessException;

    @ApiOperation(value = "保存OTA升级模块")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = true, value = "创建请求参数") @RequestBody OTAUpgradeModuleCreateRq createRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "保存OTA升级模块")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    void update(@ApiParam(required = true, value = "查修改请求参数") @RequestBody OTAUpgradeModuleUpdateRq createRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "删除OTA升级包列表")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "删除请求参数") @RequestBody OTAUpgradeModuleDeleteRq deleteRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

}
