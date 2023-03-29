package com.jike.wlw.core.equipment.ali;

import com.aliyun.iot20180120.models.*;
import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jpa.api.entity.Parts;
import com.jike.wlw.common.DateUtils;
import com.jike.wlw.common.ImportData;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.equipment.ali.imp.EquipmentImporter;
import com.jike.wlw.core.equipment.ali.iot.IemEquipmentManager;
import com.jike.wlw.core.physicalmodel.ali.iot.PhysicalModelUse;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.user.role.PRole;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.equipment.*;
import com.jike.wlw.service.equipment.ali.*;
import com.jike.wlw.service.equipment.ali.dto.BatchCheckDeviceNamesResultDTO;
import com.jike.wlw.service.equipment.ali.dto.DesiredPropertyInfoDTO;
import com.jike.wlw.service.equipment.ali.dto.PropertyInfoDTO;
import com.jike.wlw.service.equipment.dto.DeviceGroupDTO;
import com.jike.wlw.service.operation.log.OperationLog;
import com.jike.wlw.service.product.info.ProductFilter;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ApiModel("阿里云设备服务实现")
@RequestMapping(value = "service/equipment/aliyun", produces = "application/json;charset=utf-8")
public class AliEquipmentServiceImpl extends BaseService implements AliEquipmentService {

    @Autowired
    private IemEquipmentManager equipmentManager;
    @Autowired
    private PhysicalModelUse physicalModelUse;
    @Autowired
    private EquipmentImporter equipmentImporter;

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
            if (!response.getSuccess()) {
                throw new BusinessException("根据设备状态查询设备列表失败：" + response.getErrorMessage());
            }
            List<Equipment> result = new ArrayList<>();
            if (response.getData() == null || CollectionUtils.isEmpty(response.getData().getSimpleDeviceInfo())) {
                return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), response.getTotal(), result);
            }
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
            if (!response.getSuccess()) {
                throw new BusinessException("根据设备状态查询设备列表失败：" + response.getErrorMessage());
            }
            List<Equipment> result = new ArrayList<>();
            if (response.getData() == null || CollectionUtils.isEmpty(response.getData().getDeviceInfo())) {
                return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), response.getTotal(), result);
            }
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
    public ActionResult<BatchCheckDeviceNamesResultDTO> batchCheckDeviceNames(String tenantId, BatchCheckDeviceNamesRq namesRq) throws BusinessException {
        try {
            BatchCheckDeviceNamesResultDTO result = new BatchCheckDeviceNamesResultDTO();
            BatchCheckDeviceNamesResponseBody response = equipmentManager.batchCheckDeviceNames(namesRq);
            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                BeanUtils.copyProperties(response.getData(), result);
                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("批量校验设备名称失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Long> batchRegisterDeviceWithApplyId(String tenantId, BatchRegisterDeviceWithApplyIdRq applyIdRq) throws BusinessException {
        try {
            Long result = null;
            BatchRegisterDeviceWithApplyIdResponseBody response = equipmentManager.batchRegisterDeviceWithApplyId(applyIdRq);
            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                result = response.getData().getApplyId();
                return ActionResult.ok(result);
            } else {
                return ActionResult.fail("根据申请批次ID批量注册设备失败，原因：" + response.getErrorMessage());
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
            List<String> identifierList = new ArrayList<>();
            if (!StringUtil.isNullOrBlank(model.getIdentifier())) {
                identifierList.add(model.getIdentifier());
            }
            model.setIdentifierList(identifierList);
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
                if (response.getBody().getData() == null || response.getBody().getData().getList() == null || CollectionUtils.isEmpty(response.getBody().getData().getList().getPropertyInfo())) {
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

    @Override
    public ImportData batchImport(String tenantId, String productKey, String filePath) throws BusinessException {
        try {
            ImportData importData = equipmentImporter.doImport(tenantId, productKey, filePath);
            return importData;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> queryDeviceVersionBySQL(String tenantId, EquipmentSqlFilter filter) throws BusinessException {
        try {
            if (StringUtils.isBlank(filter.getProductKey())) {
                throw new BusinessException("productKey不能为空");
            }
            String countSql = "SELECT count(*) FROM device WHERE product_key =\"" + filter.getProductKey() + "\" AND status != \"DISABLE\"";
            QueryDeviceBySQLResponse countResponse = equipmentManager.queryDeviceBySQL(countSql, filter.getIotInstanceId());
            if (!countResponse.getBody().getSuccess()) {
                throw new BusinessException(countResponse.getBody().getErrorMessage());
            }
            if (countResponse.getBody().getTotalCount() == 0) {
                return new ArrayList<>();
            }
            Set<String> versionList = new HashSet<>();
            if (StringUtils.isBlank(filter.getSql())) {
                for (int i = 0; i < (countResponse.getBody().getTotalCount().intValue() + filter.getPageSize() - 1) / filter.getPageSize(); i++) {
                    String sql = "SELECT * FROM device WHERE product_key =\"" + filter.getProductKey() + "\" AND status != \"DISABLE\" limit " + i * filter.getPageSize() + "," + filter.getPageSize();
                    QueryDeviceBySQLResponse response = equipmentManager.queryDeviceBySQL(sql, filter.getIotInstanceId());
                    //这里应该转成DTO，暂时先不处理
                    if (!response.getBody().getSuccess() || CollectionUtils.isEmpty(response.getBody().getData())) {
                        continue;
                    }
                    List<String> result = response.getBody().getData().parallelStream().filter(item -> !CollectionUtils.isEmpty(item.getOTAModules())).map(QueryDeviceBySQLResponseBody.QueryDeviceBySQLResponseBodyData::getOTAModules).flatMap(Collection::parallelStream).map(QueryDeviceBySQLResponseBody.QueryDeviceBySQLResponseBodyDataOTAModules::getFirmwareVersion).distinct().collect(Collectors.toList());
                    versionList.addAll(result);
                }
                return new ArrayList<>(versionList);
            }
            ;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<DeviceGroupDTO> queryDeviceGroupList(String tenantId, QueryDeviceGroupListFilter filter) throws BusinessException {
        List<DeviceGroupDTO> deviceGroupDTOList = new ArrayList<>();
        try {
            if (filter.isAllQueryFlag()) {
                filter.setGroupTypes(null);
                deviceGroupDTOList.addAll(queryDeviceGroupList(filter));
                filter.setGroupTypes(Arrays.asList("LINK_PLATFORM_DYNAMIC"));
                deviceGroupDTOList.addAll(queryDeviceGroupList(filter));
            } else {
                deviceGroupDTOList.addAll(queryDeviceGroupList(filter));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        return deviceGroupDTOList;
    }

    private List<DeviceGroupDTO> queryDeviceGroupList(QueryDeviceGroupListFilter filter) throws BusinessException {
        List<DeviceGroupDTO> deviceGroupDTOList = new ArrayList<>();
        try {
            int page = filter.getPage() + 1;
            QueryDeviceGroupListResponse response = equipmentManager.queryDeviceGroupList(filter);
            if (!response.getBody().getSuccess() || response.getBody().getData() == null && CollectionUtils.isEmpty(response.getBody().getData().getGroupInfo())) {
                return deviceGroupDTOList;
            }
            for (QueryDeviceGroupListResponseBody.QueryDeviceGroupListResponseBodyDataGroupInfo source : response.getBody().getData().getGroupInfo()) {
                DeviceGroupDTO target = new DeviceGroupDTO();
                BeanUtils.copyProperties(source, target);
                if (StringUtils.isNotBlank(source.getUtcCreate())) {
                    target.setCreated(DateUtils.dealDateFormatUTC(source.getUtcCreate()));
                }
                deviceGroupDTOList.add(target);
            }
            if (filter.getPageSize() * filter.getPage() < response.getBody().getTotal()) {
                filter.setPage(page);
                List<DeviceGroupDTO> recursionList = queryDeviceGroupList(filter);
                deviceGroupDTOList.addAll(recursionList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        return deviceGroupDTOList;
    }
}
