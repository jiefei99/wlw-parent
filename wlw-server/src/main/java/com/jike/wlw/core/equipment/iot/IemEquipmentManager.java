package com.jike.wlw.core.equipment.iot;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.geeker123.rumba.commons.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class IemEquipmentManager {

    @Autowired
    private Environment env;
    private static DefaultAcsClient client;

    @PostConstruct
    public void initClient() {
        if (client == null) {
            try {
                String accessKeyID = env.getProperty("ali.iot.accessKey");
                String accessKeySecret = env.getProperty("ali.iot.accessSecret");
                String regionId = env.getProperty("ali.iot.regionId");
                String domain = env.getProperty("ali.iot.domain");
                String version = env.getProperty("iot.version");
                IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyID, accessKeySecret);
                DefaultProfile.addEndpoint(regionId, regionId, env.getProperty("ali.iot.productCode"),
                        domain);
                // 初始化client
                client = new DefaultAcsClient(profile);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    //@SuppressWarnings({ "unchecked", "rawtypes" })
    public AcsResponse execute(RpcAcsRequest request) {
        AcsResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (Exception e) {
            log.error("执行失败：e:" + e.getMessage());
        }
        return response;
    }


    /**
     * 注册设备
     *
     * @param ProductKey 产品名称  必须
     * @param DeviceName 设备命名  非必须
     * @return 产品创建信息
     */
    public RegisterDeviceResponse.Data registerDevice(String ProductKey, String DeviceName) {

        RegisterDeviceResponse response = null;

        RegisterDeviceRequest request = new RegisterDeviceRequest();
        request.setDeviceName(DeviceName);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("注册设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("注册设备失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (Exception e) {
            if (response != null) {
                log.error("注册设备失败！" + JSON.toJSONString(response.getData()));
            }
            throw new RuntimeException(e);
//            throw new BusinessException(e.getMessage(),e);
        }
    }

    /**
     * 删除设备。
     *
     * @param ProductKey 产品名称  非必须
     * @param DeviceName 设备的名称  非必须
     * @param IotId      设备ID    非必须
     * @return 产品创建信息
     */
    public DeleteDeviceResponse deleteDevice(String IotId, String ProductKey, String DeviceName) throws Exception {
        DeleteDeviceResponse response = null;

        DeleteDeviceRequest request = new DeleteDeviceRequest();
        request.setProductKey(ProductKey);
        request.setIotId(IotId);
        request.setDeviceName(DeviceName);

        response = client.getAcsResponse(request);
        try {


            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("删除设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("删除设备失败");
                log.error(JSON.toJSONString(response));
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("注册设备失败！" + JSON.toJSONString(response));
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 查询指定设备的详细信息
     *
     * @param IotId      设备ID  非必须
     * @param ProductKey 设备所隶属的产品Key。  非必须
     * @param DeviceName 设备名  非必须
     * @return 产品创建信息
     */
    public QueryDeviceDetailResponse.Data queryDeviceDetail(String IotId, String ProductKey, String DeviceName) {

        QueryDeviceDetailResponse response = null;
        QueryDeviceDetailRequest request = new QueryDeviceDetailRequest();
        request.setIotId(IotId);
        request.setDeviceName(DeviceName);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("查询设备详细信息成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("查询设备详细信息失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("查询设备详细信息失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 查询指定产品下的所有设备列表。
     *
     * @param ProductKey  产品名称  必须
     * @param PageSize    设备命名  非必须
     * @param CurrentPage 设备命名  非必须
     * @return 产品创建信息
     */
    public List<QueryDeviceResponse.DeviceInfo> queryDevice(String ProductKey, Integer PageSize, Integer CurrentPage) {

        QueryDeviceResponse response = null;
        QueryDeviceRequest request = new QueryDeviceRequest();
        request.setProductKey(ProductKey);
        request.setCurrentPage(CurrentPage);
        request.setPageSize(PageSize);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("产品下设备列表查询成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("产品下设备列表查询失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("产品下设备列表查询失败！" + JSON.toJSONString(response.getData()));
        }
        return null;
    }


    /**
     * 批量注册多个设备
     *
     * @param ProductKey 产品名称  必须
     * @param Count      设备的数量 必须
     * @Des 描述：
     */
    public BatchRegisterDeviceResponse.Data batchRegisterDevice(String ProductKey, Integer Count) {

        BatchRegisterDeviceResponse response = null;
        BatchRegisterDeviceRequest registerDeviceRequest = new BatchRegisterDeviceRequest();
        registerDeviceRequest.setCount(Count);
        registerDeviceRequest.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(registerDeviceRequest);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("批量命名设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("批量命名设备失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("批量命名设备失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 在指定产品下批量自定义设备名称
     *
     * @param ProductKey  产品名称  非必须
     * @param DeviceNames 设备的名称  非必须
     *                    描述：
     *                    该接口需要和BatchRegisterDeviceWithApplyId接口结合使用，实现在一个产品下批量注册（即新建）
     *                    多个设备，并且为每个设备单独命名。单次调用，最多能传入1,000 个设备名称。
     */
    public BatchCheckDeviceNamesResponse.Data batchCheckDeviceNames(String ProductKey, List<String> DeviceNames) {

        BatchCheckDeviceNamesResponse response = null;
        BatchCheckDeviceNamesRequest request = new BatchCheckDeviceNamesRequest();
        request.setDeviceNames(DeviceNames);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("批量命名设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("批量命名设备失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("批量命名设备失败！" + JSON.toJSONString(response.getData()));
        }
        return null;
    }


    /**
     * 根据申请批次ID（ApplyId）批量注册设备
     *
     * @param ProductKey 产品名称  必须
     * @param ApplyId    要批量注册的设备的申请批次ID。申请批次ID由调用BatchCheckDeviceNames接口返回。  必须
     * @Des 描述：
     * 该接口需要和BatchCheckDeviceNames接口结合使用，实现在一个产品下批量注册（即新建）多个设备，并且为每个设备单独命名。
     */
    public BatchRegisterDeviceWithApplyIdResponse.Data batchRegisterDeviceWithApplyId(String ProductKey, Long ApplyId) {

        BatchRegisterDeviceWithApplyIdResponse response = null;
        BatchRegisterDeviceWithApplyIdRequest request = new BatchRegisterDeviceWithApplyIdRequest();
        request.setApplyId(ApplyId);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("批量注册设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("批量注册设备失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("批量注册设备失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 查询批量注册设备申请的处理状态和结果
     *
     * @param ProductKey 产品名称  必须
     * @param ApplyId    设备的数量 必须
     * @Des 描述：
     */
    public QueryBatchRegisterDeviceStatusResponse.Data queryBatchRegisterDeviceStatus(String ProductKey, Long ApplyId) {
        QueryBatchRegisterDeviceStatusResponse response = null;
        QueryBatchRegisterDeviceStatusRequest request = new QueryBatchRegisterDeviceStatusRequest();
        request.setApplyId(ApplyId);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("查询批量注册设备申请成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("查询批量注册设备申请失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            log.error("查询批量注册设备申请失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 查询批量注册的设备信息
     *
     * @param ApplyId     产品名称  必须
     * @param PageSize    必须
     * @param CurrentPage 必须
     * @Des 描述：
     */
    public List<QueryPageByApplyIdResponse.ApplyDeviceInfo> queryPageByApplyId(Long ApplyId, Integer PageSize, Integer CurrentPage) {
        QueryPageByApplyIdResponse response = null;
        QueryPageByApplyIdRequest request = new QueryPageByApplyIdRequest();
        request.setApplyId(ApplyId);
        request.setCurrentPage(CurrentPage);
        request.setPageSize(PageSize);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("查询批量注册的设备信息成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("查询批量注册的设备信息失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getApplyDeviceList();

        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            log.error("查询批量注册的设备信息失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 查询指定产品下的设备统计数据
     *
     * @param ProductKey 设备所隶属的产品Key
     * @Des 描述：
     */
    public QueryDeviceStatisticsResponse.Data queryDeviceStatistics(String ProductKey) {

        QueryDeviceStatisticsResponse response = new QueryDeviceStatisticsResponse();
        QueryDeviceStatisticsRequest request = new QueryDeviceStatisticsRequest();
        request.setProductKey(ProductKey);


        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("查询批量注册的设备信息成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("查询批量注册的设备信息失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            log.error("查询批量注册的设备信息失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /********************************操作设备***************************/

    /**
     * 查看指定设备的运行状态
     *
     * @param ProductKey 产品名称  非必须
     * @param DeviceName 设备的名称  非必须
     * @param IotId      设备ID    非必须
     * @return 产品创建信息
     */
    public GetDeviceStatusResponse.Data getDeviceStatus(String ProductKey, String DeviceName, String IotId) {

        GetDeviceStatusResponse response = null;
        GetDeviceStatusRequest request = new GetDeviceStatusRequest();
        request.setProductKey(ProductKey);
        request.setDeviceName(DeviceName);
        request.setIotId(IotId);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("设备状态查询成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("设备状态查询失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getData();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("设备状态查询失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 批量查看指定设备的运行状态
     *
     * @param ProductKey  产品名称  非必须
     * @param DeviceNames 设备的名称  非必须
     * @return 产品创建信息
     */
    public List<BatchGetDeviceStateResponse.DeviceStatus> batchGetDeviceState(String ProductKey, List<String> DeviceNames) {

        BatchGetDeviceStateResponse response = null;
        BatchGetDeviceStateRequest request = new BatchGetDeviceStateRequest();
        request.setDeviceNames(DeviceNames);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("批量设备状态查询成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("批量设备状态查询失败");
                log.error(JSON.toJSONString(response));
            }
            return response.getDeviceStatusList();

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("批量设备状态查询失败！" + JSON.toJSONString(response));
        }
        return null;
    }


    /**
     * 禁用指定设备
     *
     * @param IotId      设备的名称  非必须
     * @param ProductKey 产品名称  非必须
     * @param DeviceName 设备的名称  非必须
     * @return 产品创建信息
     */
    public void disableThing(String IotId, String ProductKey, String DeviceName) {
        DisableThingResponse response = null;
        DisableThingRequest request = new DisableThingRequest();
        request.setDeviceName(DeviceName);
        request.setIotId(IotId);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("禁用设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("禁用设备失败");
                log.error(JSON.toJSONString(response));
            }

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("禁用设备失败！" + JSON.toJSONString(response));
        }
    }

    /**
     * 启用指定设备
     *
     * @param IotId      设备的名称  非必须
     * @param ProductKey 产品名称  非必须
     * @param DeviceName 设备的名称  非必须
     * @return 产品创建信息
     */
    public void enableThing(String IotId, String ProductKey, String DeviceName) {
        EnableThingResponse response = null;
        EnableThingRequest request = new EnableThingRequest();
        request.setIotId(IotId);
        request.setDeviceName(DeviceName);
        request.setProductKey(ProductKey);

        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                log.info("启用设备成功");
                log.info(JSON.toJSONString(response));
            } else {
                log.info("启用设备失败");
                log.error(JSON.toJSONString(response));
            }

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("启用设备失败！" + JSON.toJSONString(response));
        }
    }
}
