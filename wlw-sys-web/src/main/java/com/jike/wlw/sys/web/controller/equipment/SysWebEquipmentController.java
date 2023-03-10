package com.jike.wlw.sys.web.controller.equipment;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.equipment.*;
import com.jike.wlw.service.equipment.ali.BatchCheckImportDeviceResult;
import com.jike.wlw.service.equipment.ali.BatchCheckVehicleDeviceResult;
import com.jike.wlw.service.equipment.ali.BatchImportVehicleDeviceResult;
import com.jike.wlw.service.equipment.ali.ImportDeviceResult;
import com.jike.wlw.service.operation.log.OperationLog;
import com.jike.wlw.service.operation.log.OperationLogFilter;
import com.jike.wlw.sys.web.config.fegin.AliEquipmentFeignClient;
import com.jike.wlw.sys.web.config.fegin.OperationLogFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/24 10:17- zhengzhoudong - 创建。
 */
@Api(value = "操作日志服务", tags = {"操作日志服务"})
@RestController
@RequestMapping(value = "/web/equipment/aliyun", produces = "application/json;charset=utf-8")
public class SysWebEquipmentController extends BaseController {

    @Autowired
    private AliEquipmentFeignClient aliEquipmentFeignClient;

    @ApiOperation(value = "获取设备基础信息")
    @RequestMapping(value = "/getBasic", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getBasic(@ApiParam(required = true, value = "获取设备基础信息请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException{
        try {
            ActionResult<Equipment> result = aliEquipmentFeignClient.getBasic(getTenantId(), getRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取设备详细信息")
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getDetail(@ApiParam(required = true, value = "获取设备详细信息请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException{
        try {
            ActionResult<Equipment> result = aliEquipmentFeignClient.getDetail(getTenantId(), getRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建设备")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<String> create(@ApiParam(required = true, value = "新建设备请求参数") @RequestBody EquipmentCreateRq createRq) throws BusinessException{
        try {
            ActionResult<String> result = aliEquipmentFeignClient.create(getTenantId(), createRq, getUserName());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除设备")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> delete(@ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException{
        try {
            aliEquipmentFeignClient.delete(getTenantId(), getRq);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "启用设备")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> enable(@ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException{
        try {
            aliEquipmentFeignClient.enable(getTenantId(), getRq);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "禁用设备")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> disable(@ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException{
        try {
            aliEquipmentFeignClient.disable(getTenantId(), getRq);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取指定设备运行状态")
    @RequestMapping(value = "/getStatus", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getStatus(@ApiParam(required = true, value = "删除设备请求参数") @RequestBody EquipmentGetRq getRq) throws BusinessException{
        try {
            aliEquipmentFeignClient.getStatus(getTenantId(), getRq);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取设备统计数据")
    @RequestMapping(value = "/getStatistics", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Equipment> getStatistics(@ApiParam(required = true, value = "查询设备统计数据请求参数") @RequestBody EquipmentStatisticsQueryRq queryRq) throws BusinessException{
        try {
            ActionResult<Equipment> result = aliEquipmentFeignClient.getStatistics(getTenantId(), queryRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "查询设备上报过的OTA模块版本列表")
    @RequestMapping(value = "/queryOTAModuleVersions", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<PagingResult<Equipment>> queryOTAModuleVersions(@ApiParam(required = true, value = "查询条件") @RequestBody EquipmentOTAModuleVersionRq versionRq) throws BusinessException{
        try {
            PagingResult<Equipment> result = aliEquipmentFeignClient.queryOTAModuleVersions(getTenantId(), versionRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据设备状态查询设备列表")
    @RequestMapping(value = "/queryByStatus", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<PagingResult<Equipment>> queryByStatus(@ApiParam(required = true, value = "查询条件") @RequestBody EquipmentQueryByStatusRq queryRq) throws BusinessException{
        try {
            PagingResult<Equipment> result = aliEquipmentFeignClient.queryByStatus(getTenantId(), queryRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询指定产品下的设备列表")
    @RequestMapping(value = "/queryByProductKey", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<PagingResult<Equipment>> queryByProductKey(@ApiParam(required = true, value = "查询条件") @RequestBody EquipmentQueryByProductRq queryRq) throws BusinessException{
        try {
            PagingResult<Equipment> result = aliEquipmentFeignClient.queryByProductKey(getTenantId(), queryRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "云网关产品下单个导入设备")
    @RequestMapping(value = "/importDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<ImportDeviceResult> importDevice(@ApiParam(required = true, value = "查询条件") @RequestBody EquipmentImportDeviceRq importRq) throws BusinessException{
        try {
            ActionResult<ImportDeviceResult> result = aliEquipmentFeignClient.importDevice(getTenantId(), importRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "批量校验导入的设备")
    @RequestMapping(value = "/batchCheckImportDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<BatchCheckImportDeviceResult> batchCheckImportDevice(@ApiParam(required = true, value = "查询条件") @RequestBody BatchCheckImportDeviceRq importRq) throws BusinessException{
        try {
            ActionResult<BatchCheckImportDeviceResult> result = aliEquipmentFeignClient.batchCheckImportDevice(getTenantId(), importRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "云网关产品下批量导入设备")
    @RequestMapping(value = "/batchImportVehicleDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<BatchImportVehicleDeviceResult> batchImportVehicleDevice(@ApiParam(required = true, value = "查询条件") @RequestBody BatchVehicleDeviceRq importRq) throws BusinessException{
        try {
            ActionResult<BatchImportVehicleDeviceResult> result = aliEquipmentFeignClient.batchImportVehicleDevice(getTenantId(), importRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "批量校验导入的云网关设备")
    @RequestMapping(value = "/batchCheckVehicleDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<BatchCheckVehicleDeviceResult> batchCheckVehicleDevice(@ApiParam(required = true, value = "查询条件") @RequestBody BatchVehicleDeviceRq importRq) throws BusinessException{
        try {
            ActionResult<BatchCheckVehicleDeviceResult> result = aliEquipmentFeignClient.batchCheckVehicleDevice(getTenantId(), importRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
