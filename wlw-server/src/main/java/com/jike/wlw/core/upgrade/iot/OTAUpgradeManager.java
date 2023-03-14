package com.jike.wlw.core.upgrade.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.iot20180120.models.CreateOTAFirmwareRequest.CreateOTAFirmwareRequestMultiFiles;
import com.aliyun.iot20180120.models.CreateOTAStaticUpgradeJobRequest.CreateOTAStaticUpgradeJobRequestTag;
import com.aliyun.teaopenapi.models.Config;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.core.upgrade.iot.entity.OTADynamicUpgradeJobRq;
import com.jike.wlw.core.upgrade.iot.entity.OTAFirmwareRq;
import com.jike.wlw.core.upgrade.iot.entity.OTAModuleRq;
import com.jike.wlw.core.upgrade.iot.entity.OTAStaticUpgradeJobRq;
import com.jike.wlw.core.upgrade.iot.entity.OTATaskByJobCanceRq;
import com.jike.wlw.core.upgrade.iot.entity.OTATaskJobRq;
import com.jike.wlw.core.upgrade.iot.entity.OTATaskRq;
import com.jike.wlw.core.upgrade.iot.entity.OTAUpgradeJobRq;
import com.jike.wlw.core.upgrade.iot.entity.OTAUploadRq;
import com.jike.wlw.service.upgrade.ota.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
        OTAUpgradeManager upgrade=new OTAUpgradeManager();
        OTATaskRq otaTaskRq=new OTATaskRq();
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
    public GenerateOTAUploadURLResponse generateOTAUploadURL(OTAUploadRq otaUploadRq) throws Exception {
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        GenerateOTAUploadURLRequest request = new GenerateOTAUploadURLRequest();
        request.setIotInstanceId(otaUploadRq.getIotInstanceId());
        request.setFileSuffix(otaUploadRq.getFileSuffix());
        GenerateOTAUploadURLResponse response = client.generateOTAUploadURL(request);
        System.out.println("生成升级包文件上传到OSS的URL及详细信息" + JSON.toJSONString(response));
        return response;
    }

    //GenerateDeviceNameListURL
    public GenerateDeviceNameListURLResponse generateDeviceNameListURL(OTAUploadRq otaUploadRq) throws Exception {
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        GenerateDeviceNameListURLRequest request = new GenerateDeviceNameListURLRequest();
        request.setIotInstanceId(otaUploadRq.getIotInstanceId());
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
        if (CollectionUtils.isNotEmpty(createRq.getMultiFiles())&&StringUtils.isBlank(createRq.getFirmwareUrl())) {
            throw new IllegalAccessException("OTA升级包存在文件时，文件Url不能为空");
        }
        if (createRq.getType()!=null&&createRq.getType()==1&&StringUtils.isBlank(createRq.getSrcVersion())){
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
        if (CollectionUtils.isNotEmpty(createRq.getMultiFiles())){
            List<CreateOTAFirmwareRequestMultiFiles> multiFiles =new ArrayList<>();
            for (OTAFirmwareMultiFilesCreateRq multiFile : createRq.getMultiFiles()) {
                CreateOTAFirmwareRequestMultiFiles target=new CreateOTAFirmwareRequestMultiFiles();
                BeanUtils.copyProperties(multiFile,target);
                multiFiles.add(target);
            }
            request.setMultiFiles(multiFiles);
        }
        request.setNeedToVerify(createRq.isNeedToVerify());
        request.setProductKey(createRq.getProductKey());
        request.setSignMethod(createRq.getSignMethod());
        request.setSrcVersion(createRq.getSrcVersion());
        request.setType(createRq.getType());
        request.setUdi(createRq.getUdi());
        CreateOTAFirmwareResponse response = client.createOTAFirmware(request);
        System.out.println("添加升级包" + JSON.toJSONString(response));
        return response;
    }

    //DeleteOTAFirmware
    public DeleteOTAFirmwareResponse deleteOTAFirmware(String firmwareId,String iotInstanceId) throws Exception {
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
    public CreateOTAVerifyJobResponse createOTAVerifyJob(OTAUpgradeJobRq otaUpgradeJobRq) throws Exception {
        if (StringUtils.isBlank(otaUpgradeJobRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(otaUpgradeJobRq.getProductKey())) {
            throw new IllegalAccessException("产品ProductKey不能为空");
        }
        if (CollectionUtils.isEmpty(otaUpgradeJobRq.getTag())) {
            throw new IllegalAccessException("批次标签不能为空");
        }
        if (CollectionUtils.isEmpty(otaUpgradeJobRq.getTargetDeviceName())) {
            throw new IllegalAccessException("待验证设备名称不能为空");
        }
        if (otaUpgradeJobRq.getTimeoutInMinutes()!=null&&(otaUpgradeJobRq.getTimeoutInMinutes()<1||otaUpgradeJobRq.getTimeoutInMinutes()>1440)){
            throw new IllegalAccessException("设备升级超时时间范围1 ~ 1440");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTAVerifyJobRequest request = new CreateOTAVerifyJobRequest();
        request.setIotInstanceId(otaUpgradeJobRq.getIotInstanceId());
        request.setFirmwareId(otaUpgradeJobRq.getFirmwareId());
        request.setProductKey(otaUpgradeJobRq.getProductKey());
        request.setDownloadProtocol(otaUpgradeJobRq.getDownloadProtocol());
        request.setNeedConfirm(otaUpgradeJobRq.isNeedConfirm());
        request.setNeedPush(otaUpgradeJobRq.isNeedPush());
        request.setTargetDeviceName(otaUpgradeJobRq.getTargetDeviceName());
        request.setTag(otaUpgradeJobRq.getTag());
        request.setTimeoutInMinutes(otaUpgradeJobRq.getTimeoutInMinutes());
        CreateOTAVerifyJobResponse response = client.createOTAVerifyJob(request);
        System.out.println("创建升级包验证批次" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTAStaticUpgradeJob
    public CreateOTAStaticUpgradeJobResponse createOTAStaticUpgradeJob(OTAStaticUpgradeJobRq otaStaticUpgradeJobRq) throws Exception {
        if (StringUtils.isBlank(otaStaticUpgradeJobRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(otaStaticUpgradeJobRq.getProductKey())) {
            throw new IllegalAccessException("产品ProductKey不能为空");
        }
        if (StringUtils.isBlank(otaStaticUpgradeJobRq.getTargetSelection())) {
            throw new IllegalAccessException("升级范围不能为空");
        }
        if (CollectionUtils.isEmpty(otaStaticUpgradeJobRq.getTag())) {
            throw new IllegalAccessException("批次标签不能为空");
        }
        if (otaStaticUpgradeJobRq.getTag().size()>10) {
            throw new IllegalAccessException("仅支持最多添加10个批次标签");
        }
        if (otaStaticUpgradeJobRq.getSrcVersion().size()>10) {
            throw new IllegalAccessException("仅支持最多添加10个版本号");
        }
        if ("GARY".equals(otaStaticUpgradeJobRq.getTargetSelection())&&StringUtils.isBlank(otaStaticUpgradeJobRq.getGrayPercent())) {
            throw new IllegalAccessException("灰度比例不能为空");
        }
        if ("GROUP".equals(otaStaticUpgradeJobRq.getTargetSelection())&&(StringUtils.isBlank(otaStaticUpgradeJobRq.getGroupId())||StringUtils.isBlank(otaStaticUpgradeJobRq.getGroupType()))) {
            throw new IllegalAccessException("分组类型不能为空");
        }
        int tagSize=0;
        for (CreateOTAStaticUpgradeJobRequestTag tag : otaStaticUpgradeJobRq.getTag()) {
            tagSize=tag.key.length()+tag.value.length();
        }
        if (tagSize>4096){
            throw new IllegalAccessException("批次标签长度总和不能超过4096个字符");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTAStaticUpgradeJobRequest request = new CreateOTAStaticUpgradeJobRequest();
        request.setIotInstanceId(otaStaticUpgradeJobRq.getIotInstanceId());
        request.setFirmwareId(otaStaticUpgradeJobRq.getFirmwareId());
        request.setProductKey(otaStaticUpgradeJobRq.getProductKey());
        request.setDownloadProtocol(otaStaticUpgradeJobRq.getDownloadProtocol());
        request.setNeedConfirm(otaStaticUpgradeJobRq.isNeedConfirm());
        request.setNeedPush(otaStaticUpgradeJobRq.isNeedPush());
        request.setTargetDeviceName(otaStaticUpgradeJobRq.getTargetDeviceName());
        request.setTag(otaStaticUpgradeJobRq.getTag());
        request.setTimeoutInMinutes(otaStaticUpgradeJobRq.getTimeoutInMinutes());
        request.setDnListFileUrl(otaStaticUpgradeJobRq.getDnListFileUrl());
        request.setGrayPercent(otaStaticUpgradeJobRq.getGrayPercent());
        request.setGroupId(otaStaticUpgradeJobRq.getGroupId());
        request.setGroupType(otaStaticUpgradeJobRq.getGroupType());
        request.setMaximumPerMinute(otaStaticUpgradeJobRq.getMaximumPerMinute());
        request.setMultiModuleMode(otaStaticUpgradeJobRq.isMultiModuleMode());
        request.setOverwriteMode(otaStaticUpgradeJobRq.getOverwriteMode());
        request.setRetryCount(otaStaticUpgradeJobRq.getRetryCount());
        request.setRetryInterval(otaStaticUpgradeJobRq.getRetryInterval());
        request.setScheduleFinishTime(otaStaticUpgradeJobRq.getScheduleTime());
        request.setScheduleTime(otaStaticUpgradeJobRq.getScheduleTime());
        request.setSrcVersion(otaStaticUpgradeJobRq.getSrcVersion());
        request.setTargetSelection(otaStaticUpgradeJobRq.getTargetSelection());
        CreateOTAStaticUpgradeJobResponse response = client.createOTAStaticUpgradeJob(request);
        System.out.println("创建静态升级批次" + JSON.toJSONString(response));
        return response;
    }

    //CreateOTADynamicUpgradeJob
    public CreateOTADynamicUpgradeJobResponse createOTADynamicUpgradeJob(OTADynamicUpgradeJobRq otaDynamicUpgradeJobRq) throws Exception {
        if (StringUtils.isBlank(otaDynamicUpgradeJobRq.getFirmwareId())) {
            throw new IllegalAccessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(otaDynamicUpgradeJobRq.getProductKey())) {
            throw new IllegalAccessException("产品ProductKey不能为空");
        }
        if (CollectionUtils.isEmpty(otaDynamicUpgradeJobRq.getTag())) {
            throw new IllegalAccessException("批次标签不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateOTADynamicUpgradeJobRequest request = new CreateOTADynamicUpgradeJobRequest();
        request.setIotInstanceId(otaDynamicUpgradeJobRq.getIotInstanceId());
        request.setFirmwareId(otaDynamicUpgradeJobRq.getFirmwareId());
        request.setProductKey(otaDynamicUpgradeJobRq.getProductKey());
        request.setDownloadProtocol(otaDynamicUpgradeJobRq.getDownloadProtocol());
        request.setNeedConfirm(otaDynamicUpgradeJobRq.isNeedConfirm());
        request.setNeedPush(otaDynamicUpgradeJobRq.isNeedPush());
        request.setTag(otaDynamicUpgradeJobRq.getTag());
        request.setTimeoutInMinutes(otaDynamicUpgradeJobRq.getTimeoutInMinutes());
        if (CollectionUtils.isNotEmpty(otaDynamicUpgradeJobRq.getSrcVersion())){
            request.setSrcVersion(otaDynamicUpgradeJobRq.getSrcVersion());
        }else{
            request.setGroupId(otaDynamicUpgradeJobRq.getGroupId());
            request.setGroupType(otaDynamicUpgradeJobRq.getGroupId());
        }
        request.setMaximumPerMinute(otaDynamicUpgradeJobRq.getMaximumPerMinute());
        request.setMultiModuleMode(otaDynamicUpgradeJobRq.isMultiModuleMode());
        request.setOverwriteMode(otaDynamicUpgradeJobRq.getOverwriteMode());
        request.setRetryCount(otaDynamicUpgradeJobRq.getRetryCount());
        request.setRetryInterval(otaDynamicUpgradeJobRq.getRetryInterval());
        request.setDynamicMode(otaDynamicUpgradeJobRq.getDynamicMode());
        CreateOTADynamicUpgradeJobResponse response = client.createOTADynamicUpgradeJob(request);
        System.out.println("创建动态升级批次" + JSON.toJSONString(response));
        return response;
    }

    //ListOTAJobByFirmware
    public ListOTAJobByFirmwareResponse listOTAJobByFirmware(OTAUpgradePackageJobByFirmwareFilter filter) throws Exception {
        if (StringUtils.isBlank(filter.getFirmwareId())) {
            throw new IllegalAccessException("升级包ID不能为空");
        }
        if ( filter.getPageSize() > 200) {
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
        if (StringUtils.isNotBlank(filter.getDeviceNameEq())&&CollectionUtils.isEmpty(filter.getDeviceNames())){
            request.setDeviceNames(Arrays.asList(filter.getDeviceNameEq()));
        }
        if (CollectionUtils.isNotEmpty(filter.getDeviceNames())){
            request.setDeviceNames(filter.getDeviceNames());
        }
        request.setJobId(filter.getJobId());
        if (filter.getTaskStatus()!=null){
            request.setTaskStatus(filter.getTaskStatus().name());
        }
        ListOTATaskByJobResponse response = client.listOTATaskByJob(request);
        System.out.println("查询指定升级批次下的设备升级作业列表" + JSON.toJSONString(response));
        return response;
    }

    //QueryOTAJob
    public QueryOTAJobResponse queryOTAJob( String jobId, String iotInstanceId) throws Exception {
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
    public CancelOTAStrategyByJobResponse cancelOTAStrategyByJob(OTATaskJobRq otaTaskJobRq) throws Exception {
        if (StringUtils.isBlank(otaTaskJobRq.getJobId())) {
            throw new IllegalAccessException("升级批次Id不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CancelOTAStrategyByJobRequest request = new CancelOTAStrategyByJobRequest();
        request.setIotInstanceId(otaTaskJobRq.getIotInstanceId());
        request.setJobId(otaTaskJobRq.getJobId());
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
        if (CollectionUtils.isEmpty(cancelTaskByDeviceRq.getDeviceName()) || cancelTaskByDeviceRq.getDeviceName().size() > 200) {
            throw new IllegalAccessException("设备名称不能为空且最多可传入200个设备名称");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CancelOTATaskByDeviceRequest request = new CancelOTATaskByDeviceRequest();
        request.setIotInstanceId(cancelTaskByDeviceRq.getIotInstanceId());
        request.setJobId(cancelTaskByDeviceRq.getJobId());
        request.setDeviceName(cancelTaskByDeviceRq.getDeviceName());
        request.setProductKey(cancelTaskByDeviceRq.getProductKey());
        request.setFirmwareId(cancelTaskByDeviceRq.getFirmwareId());
        CancelOTATaskByDeviceResponse response = client.cancelOTATaskByDevice(request);
        System.out.println("取消指定升级包下状态为待升级的设备升级作业" + JSON.toJSONString(response));
        return response;
    }

    //CancelOTATaskByJob
    public CancelOTATaskByJobResponse cancelOTATaskByJob(OTATaskByJobCanceRq otaTaskByJobCanceRq) throws Exception {
        if (StringUtils.isBlank(otaTaskByJobCanceRq.getJobId())) {
            throw new IllegalAccessException("OTA升级批次Id不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CancelOTATaskByJobRequest request = new CancelOTATaskByJobRequest();
        request.setIotInstanceId(otaTaskByJobCanceRq.getIotInstanceId());
        request.setJobId(otaTaskByJobCanceRq.getJobId());
        request.setCancelInProgressTask(otaTaskByJobCanceRq.isCancelInProgressTask());
        request.setCancelNotifiedTask(otaTaskByJobCanceRq.isCancelNotifiedTask());
        request.setCancelQueuedTask(otaTaskByJobCanceRq.isCancelQueuedTask());
        request.setCancelUnconfirmedTask(otaTaskByJobCanceRq.isCancelUnconfirmedTask());
        request.setCancelScheduledTask(otaTaskByJobCanceRq.isCancelScheduledTask());
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
        if (StringUtils.isNotBlank(createRq.getAliasName())&&createRq.getAliasName().length() > 64) {
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
        if (StringUtils.isNotBlank(updateRq.getAliasName())&&updateRq.getAliasName().length() > 64) {
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
    public ConfirmOTATaskResponse confirmOTATask(OTATaskRq otaTaskRq) throws Exception {
        if (CollectionUtils.isEmpty(otaTaskRq.getTaskId()) || otaTaskRq.getTaskId().size() > 10) {
            throw new IllegalAccessException("待确认的设备升级作业ID不能为空且最多可传入10个TaskId");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ConfirmOTATaskRequest request = new ConfirmOTATaskRequest();
        request.setIotInstanceId(otaTaskRq.getIotInstanceId());
        request.setTaskId(otaTaskRq.getTaskId());
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
}


