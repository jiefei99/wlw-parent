package com.jike.wlw.core.equipment.ali;

import com.aliyun.iot20180120.models.*;
import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.equipment.ali.iot.IemEquipmentManager;
import com.jike.wlw.core.physicalmodel.ali.iot.PhysicalModelUse;
import com.jike.wlw.dao.TX;
import com.jike.wlw.service.equipment.*;
import com.jike.wlw.service.equipment.ali.*;
import com.jike.wlw.service.equipment.ali.dto.DesiredPropertyInfoDTO;
import com.jike.wlw.service.equipment.ali.dto.PropertyInfoDTO;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ApiModel("阿里云设备服务实现")
@RequestMapping(value = "service/equipment/aliyun", produces = "application/json;charset=utf-8")
public class AliEquipmentServiceImpl extends BaseService implements AliEquipmentService {

    @Autowired
    private IemEquipmentManager equipmentManager;
    @Autowired
    private PhysicalModelUse physicalModelUse;

    @Override
    public ActionResult<Equipment> getBasic(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            QueryDeviceInfoResponseBody response = equipmentManager.queryDeviceInfo(getRq);
            if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(response.getData(), equipment);
                equipment.setId(response.getData().getIotId());
                equipment.setName(response.getData().getDeviceName());
                equipment.setSecret(response.getData().getDeviceSecret());
                return ActionResult.ok(equipment);
            } else {
                return ActionResult.fail("获取设备基础信息失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Equipment> getDetail(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            QueryDeviceDetailResponseBody response = equipmentManager.queryDeviceDetail(getRq);
            if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(response.getData(), equipment);
                equipment.setId(response.getData().getIotId());
                equipment.setName(response.getData().getDeviceName());
                equipment.setSecret(response.getData().getDeviceSecret());
                equipment.setStatus(EquipmentStatus.valueOf(response.getData().getStatus()));
                return ActionResult.ok(equipment);
            } else {
                return ActionResult.fail("获取设备详细信息失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<String> create(String tenantId, EquipmentCreateRq createRq, String operator) throws BusinessException {
        try {
            RegisterDeviceResponseBody response = equipmentManager.registerDevice(createRq);
            if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
                return ActionResult.ok(response.getData().getIotId());
            } else {
                return ActionResult.fail("新建设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<Void> delete(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            DeleteDeviceResponseBody response = equipmentManager.deleteDevice(getRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                return ActionResult.ok();
            } else {
                return ActionResult.fail("删除设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<Void> enable(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            EnableThingResponseBody response = equipmentManager.enableThing(getRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                return ActionResult.ok();
            } else {
                return ActionResult.fail("启用设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<Void> disable(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            DisableThingResponseBody response = equipmentManager.disableThing(getRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                return ActionResult.ok();
            } else {
                return ActionResult.fail("禁用设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Equipment> getStatus(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            GetDeviceStatusResponseBody response = equipmentManager.getDeviceStatus(getRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(response.getData(), equipment);
                equipment.setStatus(EquipmentStatus.valueOf(response.getData().getStatus()));
                return ActionResult.ok(equipment);
            } else {
                return ActionResult.fail("查看指定设备的运行状态失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Equipment> getStatistics(String tenantId, EquipmentStatisticsQueryRq queryRq) throws BusinessException {
        try {
            QueryDeviceStatisticsResponseBody response = equipmentManager.queryDeviceStatistics(queryRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(response.getData(), equipment);
                return ActionResult.ok(equipment);
            } else {
                return ActionResult.fail("查看指定设备的运行状态失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> queryOTAModuleVersions(String tenantId, EquipmentOTAModuleVersionRq versionRq) throws BusinessException {
        try {
            ListOTAModuleVersionsByDeviceResponseBody response = equipmentManager.listOTAModuleVersionsByDevice(versionRq);
            if (!response.getSuccess() || response.getData() == null || CollectionUtils.isEmpty(response.getData().getSimpleOTAModuleInfo())) {
                throw new BusinessException("查询设备上报过的OTA模块版本列表失败：" + response.getErrorMessage());
            }
            List<Equipment> result = new ArrayList<>();
            for (ListOTAModuleVersionsByDeviceResponseBody.ListOTAModuleVersionsByDeviceResponseBodyDataSimpleOTAModuleInfo otaModuleInfo : response.getData().getSimpleOTAModuleInfo()) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(otaModuleInfo, equipment);
                equipment.setId(otaModuleInfo.getIotId());
                equipment.setName(otaModuleInfo.getDeviceName());
                result.add(equipment);
            }
            return new PagingResult<>(versionRq.getCurrentPage(), versionRq.getPageSize(), response.getTotal(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> queryByStatus(String tenantId, EquipmentQueryByStatusRq queryRq) throws BusinessException {
        try {
            QueryDeviceByStatusResponseBody response = equipmentManager.queryDeviceByStatus(queryRq);
            if (!response.getSuccess() || response.getData() == null || CollectionUtils.isEmpty(response.getData().getSimpleDeviceInfo())) {
                throw new BusinessException("根据设备状态查询设备列表失败：" + response.getErrorMessage());
            }
            List<Equipment> result = new ArrayList<>();
            for (QueryDeviceByStatusResponseBody.QueryDeviceByStatusResponseBodyDataSimpleDeviceInfo deviceInfo : response.getData().getSimpleDeviceInfo()) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(deviceInfo, equipment);
                equipment.setId(deviceInfo.getIotId());
                equipment.setName(deviceInfo.getDeviceName());
                equipment.setSecret(deviceInfo.getDeviceSecret());
                equipment.setStatus(EquipmentStatus.valueOf(deviceInfo.getStatus()));
                result.add(equipment);
            }
            return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), response.getTotal(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> queryByProductKey(String tenantId, EquipmentQueryByProductRq queryRq) throws BusinessException {
        try {
            QueryDeviceResponseBody response = equipmentManager.queryDeviceByProductKey(queryRq);
            if (!response.getSuccess() || response.getData() == null || CollectionUtils.isEmpty(response.getData().getDeviceInfo())) {
                throw new BusinessException("根据设备状态查询设备列表失败：" + response.getErrorMessage());
            }
            List<Equipment> result = new ArrayList<>();
            for (QueryDeviceResponseBody.QueryDeviceResponseBodyDataDeviceInfo deviceInfo : response.getData().getDeviceInfo()) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(deviceInfo, equipment);
                equipment.setId(deviceInfo.getIotId());
                equipment.setName(deviceInfo.getDeviceName());
                equipment.setSecret(deviceInfo.getDeviceSecret());
                equipment.setStatus(EquipmentStatus.valueOf(deviceInfo.getDeviceStatus()));
                result.add(equipment);
            }
            return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), response.getTotal(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<ImportDeviceResult> importDevice(String tenantId, EquipmentImportDeviceRq importRq) throws BusinessException {
        try {
            ImportDeviceResponseBody response = equipmentManager.importDevice(importRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                ImportDeviceResult result = new ImportDeviceResult();
                if (response.getData() != null) {
                    result.setDeviceName(response.getData().getDeviceName());
                    result.setDeviceSecret(response.getData().getDeviceSecret());
                    result.setIotId(response.getData().getIotId());
                    result.setNickname(response.getData().getNickname());
                    result.setProductKey(response.getData().getProductKey());
                }

                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("云网关产品下单个导入设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<BatchCheckImportDeviceResult> batchCheckImportDevice(String tenantId, BatchCheckImportDeviceRq importRq) throws BusinessException {
        try {
            BatchCheckImportDeviceResponseBody response = equipmentManager.batchCheckImportDevice(importRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                BatchCheckImportDeviceResult result = new BatchCheckImportDeviceResult();
                if (response.getData() != null) {
                    result.setInvalidDeviceNameList(response.getData().getInvalidDeviceNameList());
                    result.setInvalidDeviceSecretList(response.getData().getInvalidDeviceSecretList());
                    result.setInvalidSnList(response.getData().getInvalidSnList());
                    result.setRepeatedDeviceNameList(response.getData().getRepeatedDeviceNameList());
                }

                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("批量校验导入设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<BatchImportVehicleDeviceResult> batchImportVehicleDevice(String tenantId, BatchVehicleDeviceRq importRq) throws BusinessException {
        try {
            BatchImportVehicleDeviceResponseBody response = equipmentManager.batchImportVehicleDevice(importRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                BatchImportVehicleDeviceResult result = new BatchImportVehicleDeviceResult();
                if (response.getData() != null) {
                    result.setApplyId(response.getData().getApplyId());
                }

                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("云网关产品下批量导入设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<BatchCheckVehicleDeviceResult> batchCheckVehicleDevice(String tenantId, BatchVehicleDeviceRq importRq) throws BusinessException {
        try {
            BatchCheckVehicleDeviceResponseBody response = equipmentManager.batchCheckVehicleDevice(importRq);
            if (Boolean.TRUE.equals(response.getSuccess())) {
                BatchCheckVehicleDeviceResult result = new BatchCheckVehicleDeviceResult();
                if (response.getData() != null) {
                    result.setInvalidDeviceIdList(response.getData().getInvalidDeviceIdList());
                    result.setInvalidDeviceModelList(response.getData().getInvalidDeviceModelList());
                    result.setInvalidManufacturerList(response.getData().getInvalidManufacturerList());
                    result.setRepeatedDeviceIdList(response.getData().getRepeatedDeviceIdList());
                }

                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("批量校验导入的云网关设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Void> batchUpdateDeviceNickname(String tenantId, BatchUpdateDeviceNicknameRq nicknameRq) throws BusinessException {
        try {
            BatchUpdateDeviceNicknameResponseBody response = equipmentManager.batchUpdateDeviceNickname(nicknameRq);
            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {

                return ActionResult.ok();
            } else {
                return ActionResult.fail("批量修改设备备注名称失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<String> batchRegisterDevice(String tenantId, BatchRegisterDeviceRq deviceRq) throws BusinessException {
        try {
            BatchRegisterDeviceResponseBody response = equipmentManager.batchRegisterDevice(deviceRq.productKey, deviceRq.getCount());
            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {

                return ActionResult.ok(response.getData().getApplyId());
            } else {
                return ActionResult.fail("批量注册设备失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<List<DesiredPropertyInfoDTO>> queryDeviceDesiredProperty(String tenantId, DevicePropertyRq model) throws BusinessException {
        try {
            List<DesiredPropertyInfoDTO> result = new ArrayList<>();
            QueryDeviceDesiredPropertyResponse response = physicalModelUse.queryDeviceDesiredProperty(model);
            if (response.getBody() != null && Boolean.TRUE.equals(response.getBody().getSuccess())) {
                if (response.getBody() == null || response.getBody().getData() == null || response.getBody().getData().getList() == null || CollectionUtils.isEmpty(response.getBody().getData().getList().getDesiredPropertyInfo())) {
                    return ActionResult.ok(result);
                }
                for (QueryDeviceDesiredPropertyResponseBody.QueryDeviceDesiredPropertyResponseBodyDataListDesiredPropertyInfo source : response.getBody().getData().getList().getDesiredPropertyInfo()) {
                    DesiredPropertyInfoDTO desiredPropertyInfoDTO = new DesiredPropertyInfoDTO();
                    BeanUtils.copyProperties(source, desiredPropertyInfoDTO);
                    result.add(desiredPropertyInfoDTO);
                }

                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("设备的期望属性值失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<List<PropertyInfoDTO>> queryDevicePropertyData(String tenantId, DevicePropertyRq model) throws BusinessException {
        try {
            List<PropertyInfoDTO> result = new ArrayList<>();
            QueryDevicePropertyDataResponse response = physicalModelUse.queryDevicePropertyData(model);
            if (response.getBody() != null && Boolean.TRUE.equals(response.getBody().getSuccess())) {
                if ( response.getBody().getData() == null || response.getBody().getData().getList() == null || CollectionUtils.isEmpty(response.getBody().getData().getList().getPropertyInfo())) {
                    return ActionResult.ok(result);
                }
                for (QueryDevicePropertyDataResponseBody.QueryDevicePropertyDataResponseBodyDataListPropertyInfo source : response.getBody().getData().getList().getPropertyInfo()) {
                    PropertyInfoDTO propertyInfoDTO = new PropertyInfoDTO();
                    BeanUtils.copyProperties(source, propertyInfoDTO);
                    result.add(propertyInfoDTO);
                }

                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("查询设备的属性数据失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
