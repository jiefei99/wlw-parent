package com.jike.wlw.core.equipment.ali.iot;

import com.alibaba.fastjson.JSON;
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
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.EquipmentGetRq;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.EquipmentQueryByStatusRq;
import com.jike.wlw.service.equipment.EquipmentStatisticsQueryRq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ListOTAModuleVersionsByDeviceResponseBody listOTAModuleVersionsByDevice(EquipmentQueryByStatusRq queryByStatusRq) {
        ListOTAModuleVersionsByDeviceRequest request = new ListOTAModuleVersionsByDeviceRequest();
        BeanUtils.copyProperties(queryByStatusRq, request);

        try {
            ListOTAModuleVersionsByDeviceResponse response = client.listOTAModuleVersionsByDevice(request);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("查询设备上报过的OTA模块版本列表失败：" + JSON.toJSONString(queryByStatusRq));
                throw new BusinessException("查询设备上报过的OTA模块版本列表失败：" + JSON.toJSONString(queryByStatusRq));
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
}
