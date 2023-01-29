package com.jike.wlw.core.equipment.ali;

import com.aliyun.iot20180120.models.DeleteDeviceResponseBody;
import com.aliyun.iot20180120.models.DisableThingResponseBody;
import com.aliyun.iot20180120.models.EnableThingResponseBody;
import com.aliyun.iot20180120.models.GetDeviceStatusResponseBody;
import com.aliyun.iot20180120.models.ListOTAModuleVersionsByDeviceResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceByStatusResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceDetailResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceInfoResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceStatisticsResponseBody;
import com.aliyun.iot20180120.models.RegisterDeviceResponseBody;
import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.equipment.ali.iot.IemEquipmentManager;
import com.jike.wlw.dao.TX;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.EquipmentGetRq;
import com.jike.wlw.service.equipment.EquipmentOTAModuleVersionRq;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.EquipmentQueryByStatusRq;
import com.jike.wlw.service.equipment.EquipmentStatisticsQueryRq;
import com.jike.wlw.service.equipment.EquipmentStatus;
import com.jike.wlw.service.equipment.ali.AliEquipmentService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ApiModel("阿里云设备服务实现")
public class AliEquipmentServiceImpl extends BaseService implements AliEquipmentService {

    @Autowired
    private IemEquipmentManager equipmentManager;

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
}
