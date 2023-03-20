package com.jike.wlw.core.upgrade.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.iot20180120.models.CreateOTADynamicUpgradeJobRequest.CreateOTADynamicUpgradeJobRequestTag;
import com.aliyun.iot20180120.models.CreateOTAFirmwareRequest.CreateOTAFirmwareRequestMultiFiles;
import com.aliyun.iot20180120.models.CreateOTAStaticUpgradeJobRequest.CreateOTAStaticUpgradeJobRequestTag;
import com.aliyun.iot20180120.models.CreateOTAVerifyJobRequest.CreateOTAVerifyJobRequestTag;
import com.aliyun.teaopenapi.models.Config;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.base.Parameter;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.core.upgrade.iot.entity.OTATaskJobRq;
import com.jike.wlw.core.upgrade.iot.entity.OTATaskRq;
import com.jike.wlw.service.upgrade.ota.*;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @title: OTAUpgrade
 * @Author RS
 * @Date: 2023/1/7 11:24
 * @Version 1.0
 */
@Slf4j
@Component
public class OTAUpgradeManager {

    @Autowired
    private AliIotClient client;

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config();
        // 您的 AccessKey ID
        config.setAccessKeyId(accessKeyId);
        // 您的 AccessKey Secret
        config.setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        return new Client(config);
    }

    public static void main(String[] args) {
        OTAUpgradeManager upgrade = new OTAUpgradeManager();
        OTATaskRq otaTaskRq = new OTATaskRq();
        otaTaskRq.setTaskStatus("CONFIRM");
        otaTaskRq.setProductKey("a1GgN502dxa");
        otaTaskRq.setDeviceName("light");
        try {
            upgrade.listOTAUnfinishedTaskByDevice(otaTaskRq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //GenerateOTAUploadURL
    public GenerateOTAUploadURLResponse generateOTAUploadURL(OTAUpgradePackageGenerateUrlRq generateUrlRq) throws Exception {
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        GenerateOTAUploadURLRequest request = new GenerateOTAUploadURLRequest();
        request.setIotInstanceId(generateUrlRq.getIotInstanceId());
        request.setFileSuffix(generateUrlRq.getFileSuffix());
        GenerateOTAUploadURLResponse response = client.generateOTAUploadURL(request);
        System.out.println("生成升级包文件上传到OSS的URL及详细信息" + JSON.toJSONString(response));
        return response;
    }

    //GenerateDeviceNameListURL
    public GenerateDeviceNameListURLResponse generateDeviceNameListURL(OTAUpgradePackageGenerateDeviceNameListUrlRq generateDeviceNameListUrlRq) throws Exception {
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        GenerateDeviceNameListURLRequest request = new GenerateDeviceNameListURLRequest();
        request.setIotInstanceId(generateDeviceNameListUrlRq.getIotInstanceId());
        GenerateDeviceNameListURLResponse response = client.generateDeviceNameListURL(request);
        System.out.println("生成设备列表文件上传到OSS的URL及详细信息" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTAFirmware
    public CreateOTAFirmwareResponse createOTAFirmware(OTAUpgradePackageCreateRq createRq) throws Exception {
        if (StringUtils.isBlank(createRq.getDestVersion())) {
            throw new IllegalAccessException("当前OTA升级包的版本号不能为空");
        }
        if (StringUtils.isBlank(createRq.getFirmwareName())) {
            throw new IllegalAccessException("OTA升级包名称不能为空");
        }
        if (createRq.getType() != null && OTAUpgradePackageType.IncrementOTA.equals(createRq.getType()) && StringUtils.isBlank(createRq.getSrcVersion())) {
            throw new IllegalAccessException("使用差分升级包时，OTA模块版本号不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTAFirmwareRequest request = new CreateOTAFirmwareRequest();
        request.setIotInstanceId(createRq.getIotInstanceId());
        request.setDestVersion(createRq.getDestVersion());
        request.setFirmwareDesc(createRq.getFirmwareDesc());
        request.setFirmwareName(createRq.getFirmwareName());
        request.setFirmwareSign(createRq.getFirmwareSign());
        request.setFirmwareSize(createRq.getFirmwareSize());
        request.setFirmwareUrl(createRq.getFirmwareUrl());
        request.setModuleName(createRq.getModuleName());
        if (CollectionUtils.isNotEmpty(createRq.getMultiFiles())) {
            List<CreateOTAFirmwareRequestMultiFiles> multiFiles = new ArrayList<>();
            for (OTAFirmwareMultiFilesCreateRq multiFile : createRq.getMultiFiles()) {
                CreateOTAFirmwareRequestMultiFiles target = new CreateOTAFirmwareRequestMultiFiles();
                BeanUtils.copyProperties(multiFile, target);
                multiFiles.add(target);
            }
            request.setMultiFiles(multiFiles);
        }
        request.setNeedToVerify(createRq.isNeedToVerify());
        request.setProductKey(createRq.getProductKey());
        request.setSignMethod(createRq.getSignMethod());
        request.setSrcVersion(createRq.getSrcVersion());
        request.setType(OTAUpgradePackageType.convertMap.get(createRq.getType()));
        request.setUdi(createRq.getUdi());
        CreateOTAFirmwareResponse response = client.createOTAFirmware(request);
        System.out.println("添加升级包" + JSON.toJSONString(response));
        return response;
    }

    //DeleteOTAFirmware
    public DeleteOTAFirmwareResponse deleteOTAFirmware(String firmwareId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(firmwareId)) {
            throw new IllegalAccessException("需要删除的OTA升级包ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteOTAFirmwareRequest request = new DeleteOTAFirmwareRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setFirmwareId(firmwareId);
        DeleteOTAFirmwareResponse response = client.deleteOTAFirmware(request);
        System.out.println("删除指定升级包" + JSON.toJSONString(response));
        return response;
    }

    //ListOTAFirmware
    public ListOTAFirmwareResponse listOTAFirmware(OTAUpgradePackageFilter filter) throws Exception {
        if (filter.getPage() < 1) {
            throw new IllegalAccessException("当前页显示页数从1开始排序，请重新选择");
        }
        if (filter.getPageSize() < 0 || filter.getPageSize() > 100) {
            throw new IllegalAccessException("每页数量最大限制100条，请重新选择");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ListOTAFirmwareRequest request = new ListOTAFirmwareRequest();
        request.setIotInstanceId(filter.getIotInstanceId());
        request.setCurrentPage(filter.getPage());
        request.setProductKey(filter.getProductKey());
        request.setDestVersion(filter.getDestVersion());
        request.setPageSize(filter.getPageSize());
        ListOTAFirmwareResponse response = client.listOTAFirmware(request);
        System.out.println("查询升级包列表" + JSON.toJSONString(response));
        return response;
    }

    //QueryOTAFirmware
    public QueryOTAFirmwareResponse queryOTAFirmware(String id, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new IllegalAccessException("需要删除的OTA升级包ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryOTAFirmwareRequest request = new QueryOTAFirmwareRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setFirmwareId(id);
        QueryOTAFirmwareResponse response = client.queryOTAFirmware(request);
        System.out.println("查询指定升级包的详细信息" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTAVerifyJob
    public CreateOTAVerifyJobResponse createOTAVerifyJob(OTAUpgradePackageVerifyJobCreateRq verifyJobCreateRq) throws Exception {
        if (StringUtils.isBlank(verifyJobCreateRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(verifyJobCreateRq.getProductKey())) {
            throw new IllegalAccessException("产品ProductKey不能为空");
        }
        if (CollectionUtils.isEmpty(verifyJobCreateRq.getTargetDeviceNameIn())) {
            throw new IllegalAccessException("待验证设备名称不能为空");
        }
        if (verifyJobCreateRq.getTimeoutInMinutes() != null && (verifyJobCreateRq.getTimeoutInMinutes() < 1 || verifyJobCreateRq.getTimeoutInMinutes() > 1440)) {
            throw new IllegalAccessException("设备升级超时时间范围1 ~ 1440");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTAVerifyJobRequest request = new CreateOTAVerifyJobRequest();
        request.setIotInstanceId(verifyJobCreateRq.getIotInstanceId());
        request.setFirmwareId(verifyJobCreateRq.getFirmwareId());
        request.setProductKey(verifyJobCreateRq.getProductKey());
        request.setDownloadProtocol(verifyJobCreateRq.getDownloadProtocol());
        request.setNeedConfirm(verifyJobCreateRq.getNeedConfirm());
        request.setNeedPush(verifyJobCreateRq.getNeedPush());
        request.setTargetDeviceName(verifyJobCreateRq.getTargetDeviceNameIn());
        if (CollectionUtils.isNotEmpty(verifyJobCreateRq.getTagList())) {
            List<CreateOTAVerifyJobRequestTag> tags = new ArrayList<>();
            for (Parameter parameter : verifyJobCreateRq.getTagList()) {
                CreateOTAVerifyJobRequestTag tag = new CreateOTAVerifyJobRequestTag();
                if (StringUtils.isBlank(parameter.getName()) || StringUtils.isBlank(parameter.getValue())) {
                    continue;
                }
                tag.setKey(parameter.getName());
                tag.setValue(parameter.getValue());
                tags.add(tag);
            }
            request.setTag(tags);
        }
        request.setTimeoutInMinutes(verifyJobCreateRq.getTimeoutInMinutes());
        CreateOTAVerifyJobResponse response = client.createOTAVerifyJob(request);
        System.out.println("创建升级包验证批次" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTAStaticUpgradeJob
    public CreateOTAStaticUpgradeJobResponse createOTAStaticUpgradeJob(OTAUpgradePackageStaticUpgradeJobCreateRq staticUpgradeJobCreateRq) throws Exception {
        if (StringUtils.isBlank(staticUpgradeJobCreateRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(staticUpgradeJobCreateRq.getProductKey())) {
            throw new IllegalAccessException("产品ProductKey不能为空");
        }
        if (staticUpgradeJobCreateRq.getTargetSelection() == null) {
            throw new IllegalAccessException("升级范围不能为空");
        }
        if (CollectionUtils.isNotEmpty(staticUpgradeJobCreateRq.getTagList()) && staticUpgradeJobCreateRq.getTagList().size() > 10) {
            throw new IllegalAccessException("仅支持最多添加10个批次标签");
        }
        if (CollectionUtils.isNotEmpty(staticUpgradeJobCreateRq.getSrcVersionIn())&&staticUpgradeJobCreateRq.getSrcVersionIn().size() > 10) {
            throw new IllegalAccessException("仅支持最多添加10个版本号");
        }
        if (OTAUpgradePackageJobTargetSelectionType.GRAY.equals(staticUpgradeJobCreateRq.getTargetSelection()) && StringUtils.isBlank(staticUpgradeJobCreateRq.getGrayPercent())) {
            throw new IllegalAccessException("灰度比例不能为空");
        }
        if (OTAUpgradePackageJobTargetSelectionType.GROUP.equals(staticUpgradeJobCreateRq.getTargetSelection()) && (StringUtils.isBlank(staticUpgradeJobCreateRq.getGroupId()) || StringUtils.isBlank(staticUpgradeJobCreateRq.getGroupType()))) {
            throw new IllegalAccessException("分组类型不能为空");
        }
        int tagSize = 0;
        for (Parameter tag : staticUpgradeJobCreateRq.getTagList()) {
            tagSize = tag.getName().length() + tag.getValue().length();
        }
        if (tagSize > 4096) {
            throw new IllegalAccessException("批次标签长度总和不能超过4096个字符");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTAStaticUpgradeJobRequest request = new CreateOTAStaticUpgradeJobRequest();
        request.setIotInstanceId(staticUpgradeJobCreateRq.getIotInstanceId());
        request.setFirmwareId(staticUpgradeJobCreateRq.getFirmwareId());
        request.setProductKey(staticUpgradeJobCreateRq.getProductKey());
        request.setDownloadProtocol(staticUpgradeJobCreateRq.getDownloadProtocol());
        request.setNeedConfirm(staticUpgradeJobCreateRq.getNeedConfirm());
        request.setNeedPush(staticUpgradeJobCreateRq.getNeedPush());
        request.setTargetDeviceName(staticUpgradeJobCreateRq.getTargetDeviceNameIn());
        if (CollectionUtils.isNotEmpty(staticUpgradeJobCreateRq.getTagList())) {
            List<CreateOTAStaticUpgradeJobRequestTag> tags = new ArrayList<>();
            for (Parameter parameter : staticUpgradeJobCreateRq.getTagList()) {
                if (StringUtils.isBlank(parameter.getName()) || StringUtils.isBlank(parameter.getValue())) {
                    continue;
                }
                CreateOTAStaticUpgradeJobRequestTag tag = new CreateOTAStaticUpgradeJobRequestTag();
                tag.setKey(parameter.getName());
                tag.setValue(parameter.getValue());
                tags.add(tag);
            }
            request.setTag(tags);
        }
        request.setTimeoutInMinutes(staticUpgradeJobCreateRq.getTimeoutInMinutes());
        request.setDnListFileUrl(staticUpgradeJobCreateRq.getDnListFileUrl());
        request.setGrayPercent(staticUpgradeJobCreateRq.getGrayPercent());
        request.setGroupId(staticUpgradeJobCreateRq.getGroupId());
        request.setGroupType(staticUpgradeJobCreateRq.getGroupType());
        request.setMaximumPerMinute(staticUpgradeJobCreateRq.getMaximumPerMinute());
        request.setMultiModuleMode(staticUpgradeJobCreateRq.getMultiModuleMode());
        request.setOverwriteMode(staticUpgradeJobCreateRq.getOverwriteMode());
        request.setRetryCount(staticUpgradeJobCreateRq.getRetryCount());
        request.setRetryInterval(staticUpgradeJobCreateRq.getRetryInterval());
        if (staticUpgradeJobCreateRq.getScheduleFinishTime()!=null){
            request.setScheduleFinishTime(staticUpgradeJobCreateRq.getScheduleFinishTime().getTime());
        }
        if (staticUpgradeJobCreateRq.getScheduleTime()!=null){
            request.setScheduleTime(staticUpgradeJobCreateRq.getScheduleTime().getTime());
        }
        request.setSrcVersion(staticUpgradeJobCreateRq.getSrcVersionIn());
        request.setTargetSelection(staticUpgradeJobCreateRq.getTargetSelection().name());
        CreateOTAStaticUpgradeJobResponse response = client.createOTAStaticUpgradeJob(request);
        System.out.println("创建静态升级批次" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTADynamicUpgradeJob
    public CreateOTADynamicUpgradeJobResponse createOTADynamicUpgradeJob(OTAUpgradePackageDynamicUpgradeJobCreateRq dynamicUpgradeJobCreateRq) throws Exception {
        if (StringUtils.isBlank(dynamicUpgradeJobCreateRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(dynamicUpgradeJobCreateRq.getProductKey())) {
            throw new IllegalAccessException("产品ProductKey不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTADynamicUpgradeJobRequest request = new CreateOTADynamicUpgradeJobRequest();
        request.setIotInstanceId(dynamicUpgradeJobCreateRq.getIotInstanceId());
        request.setFirmwareId(dynamicUpgradeJobCreateRq.getFirmwareId());
        request.setProductKey(dynamicUpgradeJobCreateRq.getProductKey());
        request.setDownloadProtocol(dynamicUpgradeJobCreateRq.getDownloadProtocol());
        request.setNeedConfirm(dynamicUpgradeJobCreateRq.getNeedConfirm());
        request.setNeedPush(dynamicUpgradeJobCreateRq.getNeedPush());
        if (CollectionUtils.isNotEmpty(dynamicUpgradeJobCreateRq.getTagList())) {
            List<CreateOTADynamicUpgradeJobRequestTag> tags = new ArrayList<>();
            for (Parameter parameter : dynamicUpgradeJobCreateRq.getTagList()) {
                if (StringUtils.isBlank(parameter.getName()) || StringUtils.isBlank(parameter.getValue())) {
                    continue;
                }
                CreateOTADynamicUpgradeJobRequestTag tag = new CreateOTADynamicUpgradeJobRequestTag();
                tag.setKey(parameter.getName());
                tag.setValue(parameter.getValue());
                tags.add(tag);
            }
            request.setTag(tags);
        }
        request.setTimeoutInMinutes(dynamicUpgradeJobCreateRq.getTimeoutInMinutes());
        if (CollectionUtils.isNotEmpty(dynamicUpgradeJobCreateRq.getSrcVersionIn())) {
            request.setSrcVersion(dynamicUpgradeJobCreateRq.getSrcVersionIn());
        } else {
            request.setGroupId(dynamicUpgradeJobCreateRq.getGroupId());
            request.setGroupType(dynamicUpgradeJobCreateRq.getGroupId());
        }
        request.setMaximumPerMinute(dynamicUpgradeJobCreateRq.getMaximumPerMinute());
        request.setMultiModuleMode(dynamicUpgradeJobCreateRq.getMultiModuleMode());
        request.setOverwriteMode(dynamicUpgradeJobCreateRq.getOverwriteMode());
        request.setRetryCount(dynamicUpgradeJobCreateRq.getRetryCount());
        request.setRetryInterval(dynamicUpgradeJobCreateRq.getRetryInterval());
        request.setDynamicMode(dynamicUpgradeJobCreateRq.getDynamicMode());
        CreateOTADynamicUpgradeJobResponse response = client.createOTADynamicUpgradeJob(request);
        System.out.println("创建动态升级批次" + JSON.toJSONString(response));
        return response;
    }

    //ListOTAJobByFirmware
    public ListOTAJobByFirmwareResponse listOTAJobByFirmware(OTAUpgradePackageJobByFirmwareFilter filter) throws Exception {
        if (StringUtils.isBlank(filter.getFirmwareId())) {
            throw new IllegalAccessException("升级包ID不能为空");
        }
        if (filter.getPageSize() > 200) {
            throw new IllegalAccessException("每页显示的升级包数量不能为空且最大限制为200。");
        }
        if (filter.getPage() < 1) {
            throw new IllegalAccessException("显示返回页数不能为空且返回结果页数从1开始排序");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ListOTAJobByFirmwareRequest request = new ListOTAJobByFirmwareRequest();
        request.setIotInstanceId(filter.getIotInstanceId());
        request.setFirmwareId(filter.getFirmwareId());
        request.setCurrentPage(filter.getPage());
        request.setPageSize(filter.getPageSize());
        ListOTAJobByFirmwareResponse response = client.listOTAJobByFirmware(request);
        System.out.println("获取升级包下的升级批次列表" + JSON.toJSONString(response));
        return response;
    }

    //ListOTAJobByDevice
    public ListOTAJobByDeviceResponse listOTAJobByDevice(OTATaskJobRq otaTaskJobRq) throws Exception {
        if (StringUtils.isBlank(otaTaskJobRq.getProductKey())) {
            throw new IllegalAccessException("设备所属产品ProductKey不能为空");
        }
        if (StringUtils.isBlank(otaTaskJobRq.getDeviceName())) {
            throw new IllegalAccessException("设备名称不能为空");
        }
        if (StringUtils.isBlank(otaTaskJobRq.getFirmwareId())) {
            throw new IllegalAccessException("升级包ID不能为空");
        }
        if (otaTaskJobRq.getPageSize() == null || otaTaskJobRq.getPageSize() > 100) {
            throw new IllegalAccessException("每页显示的升级包数量不能为空且最大限制为100。");
        }
        if (otaTaskJobRq.getCurrentPage() == null || otaTaskJobRq.getCurrentPage() < 1) {
            throw new IllegalAccessException("显示返回页数不能为空且返回结果页数从1开始排序");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ListOTAJobByDeviceRequest request = new ListOTAJobByDeviceRequest();
        request.setIotInstanceId(otaTaskJobRq.getIotInstanceId());
        request.setFirmwareId(otaTaskJobRq.getFirmwareId());
        request.setCurrentPage(otaTaskJobRq.getCurrentPage());
        request.setPageSize(otaTaskJobRq.getPageSize());
        request.setProductKey(otaTaskJobRq.getProductKey());
        request.setDeviceName(otaTaskJobRq.getDeviceName());
        ListOTAJobByDeviceResponse response = client.listOTAJobByDevice(request);
        System.out.println("获取设备所在的升级包升级批次列表" + JSON.toJSONString(response));
        return response;
    }

    //ListOTATaskByJob
    public ListOTATaskByJobResponse listOTATaskByJob(OTAUpgradePackageTackByJobFilter filter) throws Exception {
        if (StringUtils.isBlank(filter.getJobId())) {
            throw new IllegalAccessException("升级批次Id不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ListOTATaskByJobRequest request = new ListOTATaskByJobRequest();
        request.setIotInstanceId(filter.getIotInstanceId());
        request.setCurrentPage(filter.getPage());
        request.setPageSize(filter.getPageSize());
        //别问 问就是ux给的是input，接口支持多选。有集合最牛
        if (StringUtils.isNotBlank(filter.getDeviceNameEq()) && CollectionUtils.isEmpty(filter.getDeviceNames())) {
            request.setDeviceNames(Arrays.asList(filter.getDeviceNameEq()));
        }
        if (CollectionUtils.isNotEmpty(filter.getDeviceNames())) {
            request.setDeviceNames(filter.getDeviceNames());
        }
        request.setJobId(filter.getJobId());
        if (filter.getTaskStatus() != null) {
            request.setTaskStatus(filter.getTaskStatus().name());
        }
        ListOTATaskByJobResponse response = client.listOTATaskByJob(request);
        System.out.println("查询指定升级批次下的设备升级作业列表" + JSON.toJSONString(response));
        return response;
    }

    //QueryOTAJob
    public QueryOTAJobResponse queryOTAJob(String jobId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(jobId)) {
            throw new IllegalAccessException("升级批次Id不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryOTAJobRequest request = new QueryOTAJobRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setJobId(jobId);
        QueryOTAJobResponse response = client.queryOTAJob(request);
        System.out.println("查询指定升级批次的详情" + JSON.toJSONString(response));
        return response;
    }

    //CancelOTAStrategyByJob
    public CancelOTAStrategyByJobResponse cancelOTAStrategyByJob(OTAUpgradePackageCancelStrategyByJobRq cancelStrategyByJobRq) throws Exception {
        if (StringUtils.isBlank(cancelStrategyByJobRq.getJobId())) {
            throw new IllegalAccessException("升级批次Id不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CancelOTAStrategyByJobRequest request = new CancelOTAStrategyByJobRequest();
        request.setIotInstanceId(cancelStrategyByJobRq.getIotInstanceId());
        request.setJobId(cancelStrategyByJobRq.getJobId());
        CancelOTAStrategyByJobResponse response = client.cancelOTAStrategyByJob(request);
        System.out.println("取消动态升级批次所关联的动态升级策略" + JSON.toJSONString(response));
        return response;
    }

    //CancelOTATaskByDevice
    public CancelOTATaskByDeviceResponse cancelOTATaskByDevice(OTAUpgradePackageCancelTaskByDeviceRq cancelTaskByDeviceRq) throws Exception {
        if (StringUtils.isBlank(cancelTaskByDeviceRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包Id不能为空");
        }
        if (StringUtils.isBlank(cancelTaskByDeviceRq.getProductKey())) {
            throw new IllegalAccessException("取消升级的设备所属产品的ProductKey不能为空");
        }
        if (CollectionUtils.isEmpty(cancelTaskByDeviceRq.getDeviceNameIn()) || cancelTaskByDeviceRq.getDeviceNameIn().size() > 10) {
            throw new IllegalAccessException("设备名称不能为空且最多可传入200个设备名称");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CancelOTATaskByDeviceRequest request = new CancelOTATaskByDeviceRequest();
        request.setIotInstanceId(cancelTaskByDeviceRq.getIotInstanceId());
        request.setJobId(cancelTaskByDeviceRq.getJobId());
        request.setDeviceName(cancelTaskByDeviceRq.getDeviceNameIn());
        request.setProductKey(cancelTaskByDeviceRq.getProductKey());
        request.setFirmwareId(cancelTaskByDeviceRq.getFirmwareId());
        CancelOTATaskByDeviceResponse response = client.cancelOTATaskByDevice(request);
        System.out.println("取消指定升级包下状态为待升级的设备升级作业" + JSON.toJSONString(response));
        return response;
    }

    //CancelOTATaskByJob
    public CancelOTATaskByJobResponse cancelOTATaskByJob(OTAUpgradePackageCancelTaskByJobRq cancelTaskByJobRq) throws Exception {
        if (StringUtils.isBlank(cancelTaskByJobRq.getJobId())) {
            throw new IllegalAccessException("OTA升级批次Id不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CancelOTATaskByJobRequest request = new CancelOTATaskByJobRequest();
        request.setIotInstanceId(cancelTaskByJobRq.getIotInstanceId());
        request.setJobId(cancelTaskByJobRq.getJobId());
        request.setCancelInProgressTask(cancelTaskByJobRq.getCancelInProgressTask());
        request.setCancelNotifiedTask(cancelTaskByJobRq.getCancelNotifiedTask());
        request.setCancelQueuedTask(cancelTaskByJobRq.getCancelQueuedTask());
        request.setCancelUnconfirmedTask(cancelTaskByJobRq.getCancelUnconfirmedTask());
        request.setCancelScheduledTask(cancelTaskByJobRq.getCancelScheduledTask());
        CancelOTATaskByJobResponse response = client.cancelOTATaskByJob(request);
        System.out.println("取消指定批次下的设备升级作业" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTAModule
    public CreateOTAModuleResponse createOTAModule(OTAUpgradeModuleCreateRq createRq) throws Exception {
        if (StringUtils.isBlank(createRq.getModuleName())) {
            throw new IllegalAccessException("OTA模块名称不能为空");
        }
        if (StringUtils.isBlank(createRq.getProductKey())) {
            throw new IllegalAccessException("OTA模块ProductKey不能为空");
        }
        if (createRq.getModuleName().length() > 64) {
            throw new IllegalAccessException("OTA模块名称长度限制为1~64个字符");
        }
        if (StringUtils.isNotBlank(createRq.getAliasName()) && createRq.getAliasName().length() > 64) {
            throw new IllegalAccessException("OTA模块别名长度限制为1~64个字符");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTAModuleRequest request = new CreateOTAModuleRequest();
        request.setIotInstanceId(createRq.getIotInstanceId());
        request.setProductKey(createRq.getProductKey());
        request.setModuleName(createRq.getModuleName());
        request.setAliasName(createRq.getAliasName());
        request.setDesc(createRq.getDetails());
        CreateOTAModuleResponse response = client.createOTAModule(request);
        System.out.println("创建产品的OTA模块" + JSON.toJSONString(response));
        return response;
    }

    //UpdateOTAModule
    public UpdateOTAModuleResponse updateOTAModule(OTAUpgradeModuleUpdateRq updateRq) throws Exception {
        if (StringUtils.isBlank(updateRq.getModuleName())) {
            throw new IllegalAccessException("OTA模块名称不能为空");
        }
        if (StringUtils.isBlank(updateRq.getProductKey())) {
            throw new IllegalAccessException("OTA模块ProductKey不能为空");
        }
        if (StringUtils.isNotBlank(updateRq.getAliasName()) && updateRq.getAliasName().length() > 64) {
            throw new IllegalAccessException("OTA模块别名长度限制为1~64个字符");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateOTAModuleRequest request = new UpdateOTAModuleRequest();
        request.setIotInstanceId(updateRq.getIotInstanceId());
        request.setProductKey(updateRq.getProductKey());
        request.setModuleName(updateRq.getModuleName());
        request.setAliasName(updateRq.getAliasName());
        request.setDesc(updateRq.getDetails());
        UpdateOTAModuleResponse response = client.updateOTAModule(request);
        System.out.println("修改OTA模块别名、描述" + JSON.toJSONString(response));
        return response;
    }

    //DeleteOTAModule
    public DeleteOTAModuleResponse deleteOTAModule(OTAUpgradeModuleDeleteRq deleteRq) throws Exception {
        if (StringUtils.isBlank(deleteRq.getModuleName())) {
            throw new IllegalAccessException("OTA模块名称不能为空");
        }
        if (StringUtils.isBlank(deleteRq.getProductKey())) {
            throw new IllegalAccessException("OTA模块ProductKey不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteOTAModuleRequest request = new DeleteOTAModuleRequest();
        request.setIotInstanceId(deleteRq.getIotInstanceId());
        request.setProductKey(deleteRq.getProductKey());
        request.setModuleName(deleteRq.getModuleName());
        DeleteOTAModuleResponse response = client.deleteOTAModule(request);
        System.out.println("删除自定义OTA模块" + JSON.toJSONString(response));
        return response;
    }

    //ListOTAModuleByProduct
    public ListOTAModuleByProductResponse listOTAModuleByProduct(OTAUpgradeModuleFilter filter) throws Exception {
        if (StringUtils.isBlank(filter.getProductKey())) {
            throw new IllegalAccessException("OTA模块ProductKey不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ListOTAModuleByProductRequest request = new ListOTAModuleByProductRequest();
        request.setIotInstanceId(filter.getIotInstanceId());
        request.setProductKey(filter.getProductKey());
        ListOTAModuleByProductResponse response = client.listOTAModuleByProduct(request);
        System.out.println("查询产品下的OTA模块列表" + JSON.toJSONString(response));
        return response;
    }

    //ConfirmOTATask
    public ConfirmOTATaskResponse confirmOTATask(OTAUpgradePackageConfirmTaskRq confirmTaskRq) throws Exception {
        if (CollectionUtils.isEmpty(confirmTaskRq.getTaskIdIn()) || confirmTaskRq.getTaskIdIn().size() > 10) {
            throw new IllegalAccessException("待确认的设备升级作业ID不能为空且最多可传入10个TaskId");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ConfirmOTATaskRequest request = new ConfirmOTATaskRequest();
        request.setIotInstanceId(confirmTaskRq.getIotInstanceId());
        request.setTaskId(confirmTaskRq.getTaskIdIn());
        ConfirmOTATaskResponse response = client.confirmOTATask(request);
        System.out.println("批量确认处于待确认状态的设备升级作业" + JSON.toJSONString(response));
        return response;
    }

    //ListOTAUnfinishedTaskByDevice
    public ListOTAUnfinishedTaskByDeviceResponse listOTAUnfinishedTaskByDevice(OTATaskRq otaTaskRq) throws Exception {
        if (StringUtils.isBlank(otaTaskRq.getTaskStatus())) {
            throw new IllegalAccessException("升级状态不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ListOTAUnfinishedTaskByDeviceRequest request = new ListOTAUnfinishedTaskByDeviceRequest();
        request.setIotInstanceId(otaTaskRq.getIotInstanceId());
        if (StringUtils.isNotBlank(otaTaskRq.getDeviceName()) && StringUtils.isNotBlank(otaTaskRq.getProductKey())) {
            request.setDeviceName(otaTaskRq.getDeviceName());
            request.setProductKey(otaTaskRq.getProductKey());
        }
        request.setTaskStatus(otaTaskRq.getTaskStatus());
        request.setModuleName(otaTaskRq.getModuleName());
        request.setIotId(otaTaskRq.getIotId());
        ListOTAUnfinishedTaskByDeviceResponse response = client.listOTAUnfinishedTaskByDevice(request);
        System.out.println("查询指定设备下，未完成状态的设备升级作业列表" + JSON.toJSONString(response));
        return response;
    }

    public ReupgradeOTATaskResponse reupgradeOTATask(OTAUpgradePackageReupgradeTaskRq taskRq) throws Exception {
        if (taskRq == null) {
            throw new BusinessException("重新升级指定批次下升级失败或升级取消的设备升级作业请求参数不能为空");
        }
        if (StringUtils.isBlank(taskRq.getJobId())) {
            throw new BusinessException("升级批次ID不能为空");
        }
        if (CollectionUtils.isEmpty(taskRq.getTaskIdIn())) {
            throw new BusinessException("设备升级作业ID不能为空");
        }
        if (taskRq.getTaskIdIn().size() > 10) {
            throw new BusinessException("TaskId个数范围为1~10个");
        }
        ReupgradeOTATaskRequest request = new ReupgradeOTATaskRequest();
        request.setIotInstanceId(taskRq.getIotInstanceId());
        request.setTaskId(taskRq.getTaskIdIn());
        request.setJobId(taskRq.getJobId());
        ReupgradeOTATaskResponse response = client.reupgradeOTATask(request);
        return response;
    }
}


