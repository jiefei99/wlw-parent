package com.jike.wlw.core.equipment.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.models.BatchCheckImportDeviceRequest;
import com.aliyun.iot20180120.models.BatchCheckImportDeviceResponse;
import com.aliyun.iot20180120.models.BatchCheckImportDeviceResponseBody;
import com.aliyun.iot20180120.models.BatchCheckVehicleDeviceRequest;
import com.aliyun.iot20180120.models.BatchCheckVehicleDeviceResponse;
import com.aliyun.iot20180120.models.BatchCheckVehicleDeviceResponseBody;
import com.aliyun.iot20180120.models.BatchImportVehicleDeviceRequest;
import com.aliyun.iot20180120.models.BatchImportVehicleDeviceResponse;
import com.aliyun.iot20180120.models.BatchImportVehicleDeviceResponseBody;
import com.aliyun.iot20180120.models.DeleteDeviceRequest;
import com.aliyun.iot20180120.models.DeleteDeviceResponse;
import com.aliyun.iot20180120.models.DeleteDeviceResponseBody;
import com.aliyun.iot20180120.models.DisableThingRequest;
import com.aliyun.iot20180120.models.DisableThingResponse;
import com.aliyun.iot20180120.models.DisableThingResponseBody;
import com.aliyun.iot20180120.models.EnableThingRequest;
import com.aliyun.iot20180120.models.EnableThingResponse;
import com.aliyun.iot20180120.models.EnableThingResponseBody;
import com.aliyun.iot20180120.models.GetDeviceStatusRequest;
import com.aliyun.iot20180120.models.GetDeviceStatusResponse;
import com.aliyun.iot20180120.models.GetDeviceStatusResponseBody;
import com.aliyun.iot20180120.models.ImportDeviceRequest;
import com.aliyun.iot20180120.models.ImportDeviceResponse;
import com.aliyun.iot20180120.models.ImportDeviceResponseBody;
import com.aliyun.iot20180120.models.ListOTAModuleVersionsByDeviceRequest;
import com.aliyun.iot20180120.models.ListOTAModuleVersionsByDeviceResponse;
import com.aliyun.iot20180120.models.ListOTAModuleVersionsByDeviceResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceByStatusRequest;
import com.aliyun.iot20180120.models.QueryDeviceByStatusResponse;
import com.aliyun.iot20180120.models.QueryDeviceByStatusResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceDetailRequest;
import com.aliyun.iot20180120.models.QueryDeviceDetailResponse;
import com.aliyun.iot20180120.models.QueryDeviceDetailResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceInfoRequest;
import com.aliyun.iot20180120.models.QueryDeviceInfoResponse;
import com.aliyun.iot20180120.models.QueryDeviceInfoResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceRequest;
import com.aliyun.iot20180120.models.QueryDeviceResponse;
import com.aliyun.iot20180120.models.QueryDeviceResponseBody;
import com.aliyun.iot20180120.models.QueryDeviceStatisticsRequest;
import com.aliyun.iot20180120.models.QueryDeviceStatisticsResponse;
import com.aliyun.iot20180120.models.QueryDeviceStatisticsResponseBody;
import com.aliyun.iot20180120.models.RegisterDeviceRequest;
import com.aliyun.iot20180120.models.RegisterDeviceResponse;
import com.aliyun.iot20180120.models.RegisterDeviceResponseBody;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.service.equipment.BatchCheckImportDeviceRq;
import com.jike.wlw.service.equipment.BatchVehicleDeviceRq;
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.EquipmentGetRq;
import com.jike.wlw.service.equipment.EquipmentImportDeviceRq;
import com.jike.wlw.service.equipment.EquipmentOTAModuleVersionRq;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.EquipmentQueryByStatusRq;
import com.jike.wlw.service.equipment.EquipmentStatisticsQueryRq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IemEquipmentManager {
    @Autowired
    private AliIotClient client;

    /**
     * 查询指定设备的基本信息
     */
    public QueryDeviceInfoResponseBody queryDeviceInfo(EquipmentGetRq getRq) {
        QueryDeviceInfoRequest request = new QueryDeviceInfoRequest();
        BeanUtils.copyProperties(getRq, request);

        try {
            QueryDeviceInfoResponse response = client.queryDeviceInfo(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("获取设备的基本信息失败：" + JSON.toJSONString(getRq));
                throw new BusinessException("获取设备的基本信息失败：" + JSON.toJSONString(getRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 查询指定设备的详细信息
     */
    public QueryDeviceDetailResponseBody queryDeviceDetail(EquipmentGetRq getRq) {
        QueryDeviceDetailRequest request = new QueryDeviceDetailRequest();
        BeanUtils.copyProperties(getRq, request);

        try {
            QueryDeviceDetailResponse response = client.queryDeviceDetail(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("获取设备的详细信息失败：" + JSON.toJSONString(getRq));
                throw new BusinessException("获取设备的详细信息失败：" + JSON.toJSONString(getRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 注册设备
     */
    public RegisterDeviceResponseBody registerDevice(EquipmentCreateRq createRq) {
        RegisterDeviceRequest request = new RegisterDeviceRequest();
        BeanUtils.copyProperties(createRq, request);

        try {
            RegisterDeviceResponse response = client.registerDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("注册设备失败：" + JSON.toJSONString(createRq));
                throw new BusinessException("注册设备失败：" + JSON.toJSONString(createRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 删除指定设备
     */
    public DeleteDeviceResponseBody deleteDevice(EquipmentGetRq getRq) {
        DeleteDeviceRequest request = new DeleteDeviceRequest();
        BeanUtils.copyProperties(getRq, request);

        try {
            DeleteDeviceResponse response = client.deleteDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("删除指定设备失败：" + JSON.toJSONString(getRq));
                throw new BusinessException("删除指定设备失败：" + JSON.toJSONString(getRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 启用指定设备
     */
    public EnableThingResponseBody enableThing(EquipmentGetRq getRq) {
        EnableThingRequest request = new EnableThingRequest();
        BeanUtils.copyProperties(getRq, request);

        try {
            EnableThingResponse response = client.enableThing(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("启用指定设备失败：" + JSON.toJSONString(getRq));
                throw new BusinessException("启用指定设备失败：" + JSON.toJSONString(getRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 禁用指定设备
     */
    public DisableThingResponseBody disableThing(EquipmentGetRq getRq) {
        DisableThingRequest request = new DisableThingRequest();
        BeanUtils.copyProperties(getRq, request);

        try {
            DisableThingResponse response = client.disableThing(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("禁用指定设备失败：" + JSON.toJSONString(getRq));
                throw new BusinessException("禁用指定设备失败：" + JSON.toJSONString(getRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 查看指定设备的运行状态
     */
    public GetDeviceStatusResponseBody getDeviceStatus(EquipmentGetRq getRq) {
        GetDeviceStatusRequest request = new GetDeviceStatusRequest();
        BeanUtils.copyProperties(getRq, request);

        try {
            GetDeviceStatusResponse response = client.getDeviceStatus(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("查看指定设备运行状态失败：" + JSON.toJSONString(getRq));
                throw new BusinessException("查看指定设备运行状态失败：" + JSON.toJSONString(getRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 查询设备统计数据
     */
    public QueryDeviceStatisticsResponseBody queryDeviceStatistics(EquipmentStatisticsQueryRq queryRq) {
        QueryDeviceStatisticsRequest request = new QueryDeviceStatisticsRequest();
        BeanUtils.copyProperties(queryRq, request);

        try {
            QueryDeviceStatisticsResponse response = client.queryDeviceStatistics(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("查询设备统计数据失败：" + JSON.toJSONString(queryRq));
                throw new BusinessException("查询设备统计数据失败：" + JSON.toJSONString(queryRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 查询设备上报过的OTA模块版本列表
     */
    public ListOTAModuleVersionsByDeviceResponseBody listOTAModuleVersionsByDevice(EquipmentOTAModuleVersionRq versionRq) {
        ListOTAModuleVersionsByDeviceRequest request = new ListOTAModuleVersionsByDeviceRequest();
        BeanUtils.copyProperties(versionRq, request);
        request.setIotId(versionRq.getId());
        request.setDeviceName(versionRq.getName());
        try {
            ListOTAModuleVersionsByDeviceResponse response = client.listOTAModuleVersionsByDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("查询设备上报过的OTA模块版本列表失败：" + JSON.toJSONString(versionRq));
                throw new BusinessException("查询设备上报过的OTA模块版本列表失败：" + JSON.toJSONString(versionRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 根据设备状态查询设备列表
     */
    public QueryDeviceByStatusResponseBody queryDeviceByStatus(EquipmentQueryByStatusRq queryByStatusRq) {
        QueryDeviceByStatusRequest request = new QueryDeviceByStatusRequest();
        BeanUtils.copyProperties(queryByStatusRq, request);

        try {
            QueryDeviceByStatusResponse response = client.queryDeviceByStatus(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("根据设备状态查询设备列表失败：" + JSON.toJSONString(queryByStatusRq));
                throw new BusinessException("根据设备状态查询设备列表失败：" + JSON.toJSONString(queryByStatusRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 查询指定产品下的设备列表
     */
    public QueryDeviceResponseBody queryDeviceByProductKey(EquipmentQueryByProductRq queryRq) {
        QueryDeviceRequest request = new QueryDeviceRequest();
        BeanUtils.copyProperties(queryRq, request);

        try {
            QueryDeviceResponse response = client.queryDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("获取设备的详细信息失败：" + JSON.toJSONString(queryRq));
                throw new BusinessException("获取设备的详细信息失败：" + JSON.toJSONString(queryRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 调用该接口在MQTT云网关产品下单个导入设备
     */
    public ImportDeviceResponseBody importDevice(EquipmentImportDeviceRq importRq) {
        ImportDeviceRequest request = new ImportDeviceRequest();
        BeanUtils.copyProperties(importRq, request);

        try {
            ImportDeviceResponse response = client.importDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("云网关产品下单个导入设备失败：" + JSON.toJSONString(importRq));
                throw new BusinessException("云网关产品下单个导入设备失败：" + JSON.toJSONString(importRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 调用该接口批量校验导入的设备
     */
    public BatchCheckImportDeviceResponseBody batchCheckImportDevice(BatchCheckImportDeviceRq importRq) {
        BatchCheckImportDeviceRequest request = new BatchCheckImportDeviceRequest();
        BeanUtils.copyProperties(importRq, request);
        if (!CollectionUtils.isEmpty(importRq.getDeviceList())) {
            List<BatchCheckImportDeviceRequest.BatchCheckImportDeviceRequestDeviceList> deviceLists = new ArrayList<>();
            for (BatchCheckImportDeviceRq.DeviceList device : importRq.getDeviceList()) {
                BatchCheckImportDeviceRequest.BatchCheckImportDeviceRequestDeviceList requestDevice = new BatchCheckImportDeviceRequest.BatchCheckImportDeviceRequestDeviceList();
                requestDevice.setDeviceName(device.getDeviceName());
                requestDevice.setDeviceSecret(device.getDeviceSecret());
                requestDevice.setSn(device.getSn());
                deviceLists.add(requestDevice);
            }
            request.setDeviceList(deviceLists);
        }

        try {
            BatchCheckImportDeviceResponse response = client.batchCheckImportDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("批量校验导入设备失败：" + JSON.toJSONString(importRq));
                throw new BusinessException("批量校验导入设备失败：" + JSON.toJSONString(importRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }


    /**
     * 调用该接口在云网关产品下批量导入设备
     */
    public BatchImportVehicleDeviceResponseBody batchImportVehicleDevice(BatchVehicleDeviceRq importRq) {
        BatchImportVehicleDeviceRequest request = new BatchImportVehicleDeviceRequest();
        BeanUtils.copyProperties(importRq, request);
        if (!CollectionUtils.isEmpty(importRq.getDeviceList())) {
            List<BatchImportVehicleDeviceRequest.BatchImportVehicleDeviceRequestDeviceList> deviceLists = new ArrayList<>();
            for (BatchVehicleDeviceRq.DeviceList device : importRq.getDeviceList()) {
                BatchImportVehicleDeviceRequest.BatchImportVehicleDeviceRequestDeviceList requestDevice = new BatchImportVehicleDeviceRequest.BatchImportVehicleDeviceRequestDeviceList();
                requestDevice.setDeviceId(device.getDeviceId());
                requestDevice.setDeviceModel(device.getDeviceModel());
                requestDevice.setManufacturer(device.getManufacturer());
                deviceLists.add(requestDevice);
            }
            request.setDeviceList(deviceLists);
        }

        try {
            BatchImportVehicleDeviceResponse response = client.batchImportVehicleDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("云网关产品下批量导入设备失败：" + JSON.toJSONString(importRq));
                throw new BusinessException("云网关产品下批量导入设备失败：" + JSON.toJSONString(importRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 调用该接口批量校验导入的云网关设备。
     */
    public BatchCheckVehicleDeviceResponseBody batchCheckVehicleDevice(BatchVehicleDeviceRq importRq) {
        BatchCheckVehicleDeviceRequest request = new BatchCheckVehicleDeviceRequest();
        BeanUtils.copyProperties(importRq, request);

        if (!CollectionUtils.isEmpty(importRq.getDeviceList())) {
            List<BatchCheckVehicleDeviceRequest.BatchCheckVehicleDeviceRequestDeviceList> deviceLists = new ArrayList<>();
            for (BatchVehicleDeviceRq.DeviceList device : importRq.getDeviceList()) {
                BatchCheckVehicleDeviceRequest.BatchCheckVehicleDeviceRequestDeviceList requestDevice = new BatchCheckVehicleDeviceRequest.BatchCheckVehicleDeviceRequestDeviceList();
                requestDevice.setDeviceId(device.getDeviceId());
                requestDevice.setDeviceModel(device.getDeviceModel());
                requestDevice.setManufacturer(device.getManufacturer());
                deviceLists.add(requestDevice);
            }
            request.setDeviceList(deviceLists);
        }

        try {
            BatchCheckVehicleDeviceResponse response = client.batchCheckVehicleDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("批量校验导入的云网关设备失败：" + JSON.toJSONString(importRq));
                throw new BusinessException("批量校验导入的云网关设备失败：" + JSON.toJSONString(importRq));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
