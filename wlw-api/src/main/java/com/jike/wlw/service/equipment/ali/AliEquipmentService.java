package com.jike.wlw.service.equipment.ali;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.equipment.BaseEquipmentService;
import com.jike.wlw.service.equipment.BatchCheckImportDeviceRq;
import com.jike.wlw.service.equipment.BatchVehicleDeviceRq;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.EquipmentGetRq;
import com.jike.wlw.service.equipment.EquipmentImportDeviceRq;
import com.jike.wlw.service.equipment.EquipmentOTAModuleVersionRq;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.EquipmentQueryByStatusRq;
import com.jike.wlw.service.equipment.EquipmentStatisticsQueryRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "阿里云设备服务")
@RequestMapping(value = "service/equipment/aliyun", produces = "application/json;charset=utf-8")
public interface AliEquipmentService extends BaseEquipmentService {

    @ApiOperation(value = "获取设备基础信息")
    @RequestMapping(value = "/getBasic", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getBasic(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "获取设备基础信息请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException;

    @ApiOperation(value = "获取设备详细信息")
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getDetail(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "获取设备详细信息请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException;

    @ApiOperation(value = "新建设备")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<String> create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "新建设备请求参数") @RequestBody EquipmentCreateRq createRq,
                                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "删除设备")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException;

    @ApiOperation(value = "启用设备")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> enable(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException;

    @ApiOperation(value = "禁用设备")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> disable(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException;

    @ApiOperation(value = "获取指定设备运行状态")
    @RequestMapping(value = "/getStatus", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getStatus(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException;

    @ApiOperation(value = "获取设备统计数据")
    @RequestMapping(value = "/getStatistics", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getStatistics(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询设备统计数据请求参数") @RequestBody EquipmentStatisticsQueryRq queryRq) throws BusinessException;


    @ApiOperation(value = "查询设备上报过的OTA模块版本列表")
    @RequestMapping(value = "/queryOTAModuleVersions", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Equipment> queryOTAModuleVersions(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody EquipmentOTAModuleVersionRq versionRq) throws BusinessException;

    @ApiOperation(value = "根据设备状态查询设备列表")
    @RequestMapping(value = "/queryByStatus", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Equipment> queryByStatus(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody EquipmentQueryByStatusRq queryRq) throws BusinessException;

    @ApiOperation(value = "查询指定产品下的设备列表")
    @RequestMapping(value = "/queryByProductKey", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Equipment> queryByProductKey(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody EquipmentQueryByProductRq queryRq) throws BusinessException;

    @ApiOperation(value = "云网关产品下单个导入设备")
    @RequestMapping(value = "/importDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<ImportDeviceResult> importDevice(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody EquipmentImportDeviceRq importRq) throws BusinessException;


    @ApiOperation(value = "批量校验导入的设备")
    @RequestMapping(value = "/batchCheckImportDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<BatchCheckImportDeviceResult> batchCheckImportDevice(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody BatchCheckImportDeviceRq importRq) throws BusinessException;

    @ApiOperation(value = "云网关产品下批量导入设备")
    @RequestMapping(value = "/batchImportVehicleDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<BatchImportVehicleDeviceResult> batchImportVehicleDevice(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody BatchVehicleDeviceRq importRq) throws BusinessException;

    @ApiOperation(value = "批量校验导入的云网关设备")
    @RequestMapping(value = "/batchCheckVehicleDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<BatchCheckVehicleDeviceResult> batchCheckVehicleDevice(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId, @ApiParam(required = true, value = "查询条件") @RequestBody BatchVehicleDeviceRq importRq) throws BusinessException;
}
