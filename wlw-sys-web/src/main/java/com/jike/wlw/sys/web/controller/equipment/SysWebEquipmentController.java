package com.jike.wlw.sys.web.controller.equipment;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.ImportData;
import com.jike.wlw.service.equipment.*;
import com.jike.wlw.service.equipment.ali.*;
import com.jike.wlw.service.equipment.ali.dto.DesiredPropertyInfoDTO;
import com.jike.wlw.service.equipment.ali.dto.PropertyInfoDTO;
import com.jike.wlw.service.equipment.dto.DeviceGroupDTO;
import com.jike.wlw.service.equipment.vo.DeviceGroupVO;
import com.jike.wlw.service.equipment.vo.EquipmentByNameVO;
import com.jike.wlw.sys.web.config.fegin.AliEquipmentFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @ApiOperation(value = "获取设备名称")
    @RequestMapping(value = "/queryName", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<PagingResult<EquipmentByNameVO>> queryName(@ApiParam(required = true, value = "获取设备详细信息请求参数") @RequestBody EquipmentQueryNameByProductRq filter) throws BusinessException{
        try {
            List<EquipmentByNameVO> list=new ArrayList();
            if (StringUtils.isNotBlank(filter.getName())){
                EquipmentGetRq equipmentGetRq=new EquipmentGetRq();
                equipmentGetRq.setProductKey(filter.getProductKey());
                equipmentGetRq.setDeviceName(filter.getName());
                ActionResult<Equipment> detail = aliEquipmentFeignClient.getDetail(getTenantId(), equipmentGetRq);
                if (detail==null||detail.getData()==null){
                    return ActionResult.ok(new PagingResult<>(0,0,0,list));
                }
                EquipmentByNameVO equipmentByNameVO=new EquipmentByNameVO();
                BeanUtils.copyProperties(detail.getData(),equipmentByNameVO);
                list.add(equipmentByNameVO);
                return ActionResult.ok(new PagingResult<>(filter.getCurrentPage(),filter.getPageSize(),1,list));
            }else{
                PagingResult<Equipment> result = aliEquipmentFeignClient.queryByProductKey(getTenantId(), filter);
                if (result.getData()==null|| CollectionUtils.isEmpty(result.getData())){
                    return ActionResult.ok(new PagingResult<>(0,0,0,list));
                }
                for (Equipment source : result.getData()) {
                    EquipmentByNameVO equipmentByNameVO=new EquipmentByNameVO();
                    BeanUtils.copyProperties(source,equipmentByNameVO);
                    list.add(equipmentByNameVO);
                }
                return ActionResult.ok(new PagingResult<>(result.getPage(),result.getPageSize(),result.getTotal(),list));
            }
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建设备")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<String> create(@ApiParam(required = true, value = "新建设备请求参数") @RequestBody EquipmentCreateRq createRq) throws BusinessException{
        try {
            ActionResult<String> result = aliEquipmentFeignClient.create(getTenantId(), createRq, "管理员");

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

    @ApiOperation(value = "批量修改设备备注名称")
    @RequestMapping(value = "/batchUpdateDeviceNickname", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<Void> batchUpdateDeviceNickname(@ApiParam(required = true, value = "查询条件") @RequestBody BatchUpdateDeviceNicknameRq nicknameRq) throws BusinessException{
        try {
            aliEquipmentFeignClient.batchUpdateDeviceNickname(getTenantId(), nicknameRq);

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "在指定产品下批量注册多个设备")
    @RequestMapping(value = "/batchRegisterDevice", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<String> batchRegisterDevice(@ApiParam(required = true, value = "批量注册多个设备请求参数") @RequestBody BatchRegisterDeviceRq deviceRq) throws BusinessException{
        try {
            ActionResult<String> result = aliEquipmentFeignClient.batchRegisterDevice(getTenantId(), deviceRq);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询指定设备的期望属性值")
    @RequestMapping(value = "/queryDeviceDesiredProperty", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<List<DesiredPropertyInfoDTO>> queryDeviceDesiredProperty(@ApiParam(required = true, value = "查询条件") @RequestBody DevicePropertyRq model) throws BusinessException{
        try {
            ActionResult<List<DesiredPropertyInfoDTO>> result = aliEquipmentFeignClient.queryDeviceDesiredProperty(getTenantId(), model);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询指定设备或数字孪生节点，在指定时间段内，单个属性的数据")
    @RequestMapping(value = "/queryDevicePropertyData", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<List<PropertyInfoDTO>> queryDevicePropertyData(@ApiParam(required = true, value = "查询条件") @RequestBody DevicePropertyRq model) throws BusinessException{
        try {
            ActionResult<List<PropertyInfoDTO>> result = aliEquipmentFeignClient.queryDevicePropertyData(getTenantId(), model);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "设备信息导入")
    @RequestMapping(value = "/batchImport", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<ImportData> batchImport(@ApiParam(required = true, value = "产品Key") @RequestBody BatchImportRq importRq) throws BusinessException{
        try {
            ImportData result = aliEquipmentFeignClient.batchImport(getTenantId(), importRq.getProductKey(), importRq.getFilePath());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "sql批量查询库存")
    @RequestMapping(value = "/batchSqlQueryByVersion", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<List<String>> batchSqlQueryByVersion(@ApiParam(required = true, value = "") @RequestBody EquipmentSqlFilter filter) throws BusinessException{
        try {
            if (StringUtils.isBlank(filter.getProductKey())){
                throw new BusinessException("productKey不能为空");
            }
            List<String> result = aliEquipmentFeignClient.queryDeviceVersionBySQL(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询分组")
    @RequestMapping(value = "/queryDeviceGroupList", method = RequestMethod.POST)
    @ResponseBody
    ActionResult<List<DeviceGroupVO>> queryDeviceGroupList(@ApiParam(required = true, value = "") @RequestBody QueryDeviceGroupListFilter filter) throws BusinessException{
        ArrayList<DeviceGroupVO> groupVOArrayList = new ArrayList<>();
        try {
            List<DeviceGroupDTO> result = aliEquipmentFeignClient.queryDeviceGroupList(getTenantId(), filter);
            if (CollectionUtils.isEmpty(result)){
                return ActionResult.ok(groupVOArrayList);
            }
            for (DeviceGroupDTO deviceGroupDTO : result) {
                DeviceGroupVO vo=new DeviceGroupVO();
                BeanUtils.copyProperties(deviceGroupDTO,vo);
                groupVOArrayList.add(vo);
            }
            return ActionResult.ok(groupVOArrayList);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
