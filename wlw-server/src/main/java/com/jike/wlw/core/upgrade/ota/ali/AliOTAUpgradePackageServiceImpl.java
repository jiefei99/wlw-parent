package com.jike.wlw.core.upgrade.ota.ali;

import com.aliyun.iot20180120.models.*;
import com.aliyun.iot20180120.models.ListOTAFirmwareResponseBody.ListOTAFirmwareResponseBodyFirmwareInfoSimpleFirmwareInfo;
import com.aliyun.iot20180120.models.ListOTAJobByFirmwareResponseBody.ListOTAJobByFirmwareResponseBodyDataSimpleOTAJobInfo;
import com.aliyun.iot20180120.models.ListOTATaskByJobResponseBody.ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo;
import com.aliyun.iot20180120.models.QueryOTAFirmwareResponseBody.QueryOTAFirmwareResponseBodyFirmwareInfoMultiFiles;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.DateUtils;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.equipment.imp.EquipmentNameImporter;
import com.jike.wlw.core.upgrade.iot.OTAUpgradeManager;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCancelStrategyByJobRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCancelTaskByDeviceRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCancelTaskByJobRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageConfirmTaskRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDynamicUpgradeJobCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageGenerateDeviceNameListUrlRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageGenerateUrlRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobByFirmwareFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobSelectionType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobTargetSelectionType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageReupgradeTaskRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStaticUpgradeJobCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTackByJobFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageVerifyJobCreateRq;
import com.jike.wlw.service.upgrade.ota.ali.AliOTAUpgradePackageService;
import com.jike.wlw.service.upgrade.ota.dto.OTAFirmwareInfoMultiFilesDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageGenerateUrlInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageListDeviceTaskByJobDTO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: AliOTAUpgradePackageServiceImpl
 * @Author RS
 * @Date: 2023/3/8 17:40
 * @Version 1.0
 */

@Slf4j
@RestController("OTAUpgradePackageServiceAliImpl")
@ApiModel("阿里OTA升级包实现")
@RequestMapping(value = "service/aliOTAUpgradePackage", produces = "application/json;charset=utf-8")
//todo 这些其实应该用DTO类，不能直接使用VO的,目前只改了部分
public class AliOTAUpgradePackageServiceImpl extends BaseService implements AliOTAUpgradePackageService {

    @Autowired
    private OTAUpgradeManager otaUpgradeManager;
    @Autowired
    private EquipmentNameImporter equipmentNameImporter;

    @Override
    public PagingResult<OTAUpgradePackageListVO> query(String tenantId, OTAUpgradePackageFilter filter) throws BusinessException {
        List<OTAUpgradePackageListVO> otaUpgradePackageVOList = new ArrayList<>();
        int total = 0;
        ListOTAFirmwareResponse response=null;
        try {
            response = otaUpgradeManager.listOTAFirmware(filter);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response==null || response.getBody() == null  ||
                    response.getBody().getFirmwareInfo() == null || CollectionUtils.isEmpty(response.getBody().getFirmwareInfo().getSimpleFirmwareInfo())) {
                return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, otaUpgradePackageVOList);
            }
            for (ListOTAFirmwareResponseBodyFirmwareInfoSimpleFirmwareInfo firmwareInfo : response.getBody().getFirmwareInfo().getSimpleFirmwareInfo()) {
                OTAUpgradePackageListVO otaUpgradePackageListVO = new OTAUpgradePackageListVO();
                BeanUtils.copyProperties(firmwareInfo, otaUpgradePackageListVO);
                if (firmwareInfo.getType() != null) {
                    otaUpgradePackageListVO.setType(OTAUpgradePackageType.map.get(firmwareInfo.getType()));
                }
                if (firmwareInfo.getStatus() != null) {
                    otaUpgradePackageListVO.setStatusType(OTAUpgradePackageStatusType.map.get(firmwareInfo.getStatus()));
                }
                if (StringUtils.isNotBlank(firmwareInfo.getUtcCreate())) {
                    otaUpgradePackageListVO.setCreated(DateUtils.dealDateFormatUTC(firmwareInfo.getUtcCreate()));
                }
                otaUpgradePackageVOList.add(otaUpgradePackageListVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PagingResult<>(filter.getPage(), filter.getPageSize(), response.getBody().getTotal(), otaUpgradePackageVOList);
    }

    @Override
    public PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> queryEquipment(String tenantId, OTAUpgradePackageTackByJobFilter filter) throws BusinessException {
        if (filter == null || StringUtils.isBlank(filter.getJobId())) {
            throw new BusinessException("升级批次ID不能为空");
        }
        List<OTAUpgradePackageListDeviceTaskByJobDTO> list = new ArrayList<>();
        try {
            ListOTATaskByJobResponse response = otaUpgradeManager.listOTATaskByJob(filter);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null || response.getBody() == null || response.getBody().getData() == null || CollectionUtils.isEmpty(response.getBody().getData().getSimpleOTATaskInfo())) {
                return new PagingResult<>(filter.getPage(), filter.getPageSize(), 0, list);
            }
            for (ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo source : response.getBody().getData().getSimpleOTATaskInfo()) {
                OTAUpgradePackageListDeviceTaskByJobDTO target = new OTAUpgradePackageListDeviceTaskByJobDTO();
                BeanUtils.copyProperties(source, target);
                if (StringUtils.isNotBlank(source.getTaskStatus())) {
                    target.setTaskStatus(OTAUpgradePackageTaskStatusType.valueOf(source.getTaskStatus()));
                }
                if (StringUtils.isNotBlank(source.getUtcCreate())) {
                    target.setCreated(DateUtils.dealDateFormatUTC(source.getUtcCreate()));
                }
                if (StringUtils.isNotBlank(source.getUtcModified())) {
                    target.setModified(DateUtils.dealDateFormatUTC(source.getUtcModified()));
                }
                list.add(target);
            }
            return new PagingResult<>(filter.getPage(), filter.getPageSize(), response.getBody().getTotal(), list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PagingResult<OTAUpgradePackageJobBatchListVO> queryJobByFirmware(String tenantId, OTAUpgradePackageJobByFirmwareFilter filter) throws BusinessException {
        if (filter == null || StringUtils.isBlank(filter.getFirmwareId())) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        List<OTAUpgradePackageJobBatchListVO> otaUpgradePackageJobBatchListVO = new ArrayList<>();
        ListOTAJobByFirmwareResponse response = null;
        try {
            response = otaUpgradeManager.listOTAJobByFirmware(filter);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null || response.getBody() == null || response.getBody().getData() == null || CollectionUtils.isEmpty(response.getBody().getData().getSimpleOTAJobInfo())) {
                return new PagingResult<>(filter.getPage(), filter.getPageSize(), 0, otaUpgradePackageJobBatchListVO);
            }
            for (ListOTAJobByFirmwareResponseBodyDataSimpleOTAJobInfo source : response.getBody().getData().getSimpleOTAJobInfo()) {
                OTAUpgradePackageJobBatchListVO vo = new OTAUpgradePackageJobBatchListVO();
                vo.setProductKey(source.getProductKey());
                vo.setJobId(source.getJobId());
                if (StringUtils.isNotBlank(source.getJobStatus())) {
                    vo.setJobStatusType(OTAUpgradePackageJobStatusType.valueOf(source.getJobStatus()));
                }
                if (StringUtils.isNotBlank(source.getSelectionType())) {
                    vo.setSelectionType(OTAUpgradePackageJobSelectionType.valueOf(source.getSelectionType()));
                }
                if (StringUtils.isNotBlank(source.getJobType())) {
                    vo.setJobType(OTAUpgradePackageJobType.valueOf(source.getJobType()));
                }
                otaUpgradePackageJobBatchListVO.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PagingResult<>(filter.getPage(), filter.getPageSize(), response.getBody().getTotal(), otaUpgradePackageJobBatchListVO);
    }

    @Override
    public OTAUpgradePackageInfoDTO getInfo(String tenantId, String id, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("当前OTA升级包ID不能为空");
        }
        OTAUpgradePackageInfoDTO otaUpgradePackageInfoDTO = new OTAUpgradePackageInfoDTO();
        try {
            QueryOTAFirmwareResponse response = otaUpgradeManager.queryOTAFirmware(id, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null  || response.getBody().getFirmwareInfo() == null) {
                return otaUpgradePackageInfoDTO;
            }
            BeanUtils.copyProperties(response.getBody().getFirmwareInfo(), otaUpgradePackageInfoDTO);
            if (StringUtils.isNotBlank(response.getBody().getFirmwareInfo().getUtcCreate())) {
                otaUpgradePackageInfoDTO.setCreated(DateUtils.dealDateFormatUTC(response.getBody().getFirmwareInfo().getUtcCreate()));
            }
            if (StringUtils.isNotBlank(response.getBody().getFirmwareInfo().getUtcModified())) {
                otaUpgradePackageInfoDTO.setModified(DateUtils.dealDateFormatUTC(response.getBody().getFirmwareInfo().getUtcModified()));
            }
            if (response.getBody().getFirmwareInfo().getStatus() != null) {
                otaUpgradePackageInfoDTO.setStatus(OTAUpgradePackageStatusType.map.get(response.getBody().getFirmwareInfo().getStatus()));
            }
            if (response.getBody().getFirmwareInfo().getType() != null) {
                otaUpgradePackageInfoDTO.setType(OTAUpgradePackageType.map.get(response.getBody().getFirmwareInfo().getType()));
            }
            List<OTAFirmwareInfoMultiFilesDTO> multiFiles = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(response.getBody().getFirmwareInfo().getMultiFiles())) {
                for (QueryOTAFirmwareResponseBodyFirmwareInfoMultiFiles multiFile : response.getBody().getFirmwareInfo().getMultiFiles()) {
                    OTAFirmwareInfoMultiFilesDTO multiFilesDTO = new OTAFirmwareInfoMultiFilesDTO();
                    BeanUtils.copyProperties(multiFile, multiFilesDTO);
                    multiFiles.add(multiFilesDTO);
                }
            }
            otaUpgradePackageInfoDTO.setMultiFiles(multiFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otaUpgradePackageInfoDTO;
    }

    @Override
    public OTAUpgradePackageJobBatchInfoVO getJobBatchInfo(String tenantId, String jobId, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(jobId)) {
            throw new BusinessException("升级批次ID不能为空");
        }
        OTAUpgradePackageJobBatchInfoVO otaUpgradePackageJobBatchInfoVO = new OTAUpgradePackageJobBatchInfoVO();
        try {
            QueryOTAJobResponse response = otaUpgradeManager.queryOTAJob(jobId, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null  || response.getBody().getData() == null) {
                return otaUpgradePackageJobBatchInfoVO;
            }
            BeanUtils.copyProperties(response.getBody().getData(), otaUpgradePackageJobBatchInfoVO);
            if (StringUtils.isNotBlank(response.getBody().getData().getJobStatus())) {
                otaUpgradePackageJobBatchInfoVO.setJobStatusType(OTAUpgradePackageJobStatusType.valueOf(response.getBody().getData().getJobStatus()));
            }
            if (StringUtils.isNotBlank(response.getBody().getData().getTargetSelection())) {
                otaUpgradePackageJobBatchInfoVO.setTargetSelectionType(OTAUpgradePackageJobTargetSelectionType.valueOf(response.getBody().getData().getTargetSelection()));
            }
            if (StringUtils.isNotBlank(response.getBody().getData().getSelectionType())) {
                otaUpgradePackageJobBatchInfoVO.setSelectionType(OTAUpgradePackageJobSelectionType.valueOf(response.getBody().getData().getSelectionType()));
            }
            if (StringUtils.isNotBlank(response.getBody().getData().getUtcStartTime())){
                otaUpgradePackageJobBatchInfoVO.setUtcStartDate(DateUtils.dealDateFormatUTC(response.getBody().getData().getUtcStartTime()));
            }
            if (response.getBody().getData().getSrcVersions() != null) {
                otaUpgradePackageJobBatchInfoVO.setSrcVersionList(response.getBody().getData().getSrcVersions().getSrcVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otaUpgradePackageJobBatchInfoVO;
    }

    @Override
    public OTAUpgradePackageVO get(String tenantId, String id, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("当前OTA升级包ID不能为空");
        }
        OTAUpgradePackageVO otaUpgradePackageVO = new OTAUpgradePackageVO();
        try {
            // 先查询OTA升级包的基本信息
            QueryOTAFirmwareResponse response = otaUpgradeManager.queryOTAFirmware(id, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null || response.getBody().getFirmwareInfo() == null) {
                throw new BusinessException("找不到此OTA升级包");
            }
            BeanUtils.copyProperties(response.getBody().getFirmwareInfo(), otaUpgradePackageVO);
            if (response.getBody().getFirmwareInfo().getType() != null) {
                otaUpgradePackageVO.setType(OTAUpgradePackageType.map.get(response.getBody().getFirmwareInfo().getType()));
            }
            return otaUpgradePackageVO;
//            //计算目标设备数量
//            OTAUpgradePackageTackByJobFilter filter = new OTAUpgradePackageTackByJobFilter();
//            filter.setJobId(id);
//            filter.setIotInstanceId(iotInstanceId);
//            filter.setPage(1);
//            filter.setPageSize(100);
//            ListOTATaskByJobResponse listOTATaskByJobResponse = otaUpgradeManager.listOTATaskByJob(filter);
//            if (!listOTATaskByJobResponse.getBody().getSuccess() || listOTATaskByJobResponse.getBody().getData() == null || CollectionUtils.isEmpty(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo())) {
//                return otaUpgradePackageVO;
//            }
//            List<ListOTATaskByJobResponseBody.ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo> taskInfoList = new ArrayList<>();
//            taskInfoList.addAll(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo());
//            int forCount = (listOTATaskByJobResponse.getBody().getTotal() + 100 - 1) / 100; //总数不止一页的情况
//            for (int i = 0; i < forCount - 1; i++) {
//                filter.setPage(filter.getPage() + 1);
//                listOTATaskByJobResponse = otaUpgradeManager.listOTATaskByJob(filter);
//                if (!listOTATaskByJobResponse.getBody().getSuccess() || listOTATaskByJobResponse.getBody().getData() == null || CollectionUtils.isEmpty(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo())) {
//                    continue;
//                }
//                taskInfoList.addAll(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo());
//            }
//            int targetDeviceUpgradeTotal = 0;
//            int targetSuccessTotal = 0;
//            int targetFailTotal = 0;
//            int targetCancelTotal = 0;
//            for (ListOTATaskByJobResponseBody.ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo listOTATaskByJobResponseBodyDataSimpleOTATaskInfo : taskInfoList) {
//                if ("SUCCEEDED".equals(listOTATaskByJobResponseBodyDataSimpleOTATaskInfo.getTaskStatus())) {
//                    targetSuccessTotal++;
//                }
//                if ("FAILED".equals(listOTATaskByJobResponseBodyDataSimpleOTATaskInfo.getTaskStatus())) {
//                    targetFailTotal++;
//                }
//                if ("CANCELED".equals(listOTATaskByJobResponseBodyDataSimpleOTATaskInfo.getTaskStatus())) {
//                    targetCancelTotal++;
//                }
//                targetDeviceUpgradeTotal++;
//            }
//            otaUpgradePackageVO.setTargetDeviceUpgradeTotal(targetDeviceUpgradeTotal);
//            otaUpgradePackageVO.setTargetCancelTotal(targetCancelTotal);
//            otaUpgradePackageVO.setTargetSuccessTotal(targetSuccessTotal);
//            otaUpgradePackageVO.setTargetFailTotal(targetFailTotal);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void create(String tenantId, OTAUpgradePackageCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(createRq.getDestVersion())) {
            throw new BusinessException("当前OTA升级包版本号不能为空");
        }
        if (StringUtils.isBlank(createRq.getFirmwareName())) {
            throw new BusinessException("升级包名称不能为空");
        }
        try {
            CreateOTAFirmwareResponse response = otaUpgradeManager.createOTAFirmware(createRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void delete(OTAUpgradePackageDeleteRq deleteRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(deleteRq.getFirmwareId())) {
            throw new BusinessException("升级包id不能为空");
        }
        try {
            DeleteOTAFirmwareResponse response = otaUpgradeManager.deleteOTAFirmware(deleteRq.getFirmwareId(), deleteRq.getIotInstanceId());
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public Boolean cancelOTATaskByDevice(OTAUpgradePackageCancelTaskByDeviceRq cancelTaskByDeviceRq, String operator) throws BusinessException {
        if (cancelTaskByDeviceRq == null) {
            throw new BusinessException("取消设备升级参数不能为空");
        }
        if (StringUtils.isBlank(cancelTaskByDeviceRq.getProductKey())) {
            throw new BusinessException("ProductKey不能为空");
        }
        if (StringUtils.isBlank(cancelTaskByDeviceRq.getFirmwareId())) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            CancelOTATaskByDeviceResponse resp = otaUpgradeManager.cancelOTATaskByDevice(cancelTaskByDeviceRq);
            if (!resp.getBody().getSuccess()){
                throw new BusinessException(resp.getBody().getErrorMessage());
            }
            if (resp == null || resp.getBody() == null) {
                return false;
            }
            return resp.getBody().getSuccess();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public String createOTAVerifyJob(OTAUpgradePackageVerifyJobCreateRq verifyJobCreateRq, String operator) throws BusinessException {
        if (verifyJobCreateRq == null) {
            throw new BusinessException("OTA升级包验证任务请求参数不能为空");
        }
        if (StringUtils.isBlank(verifyJobCreateRq.getFirmwareId())) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        if (StringUtils.isBlank(verifyJobCreateRq.getProductKey())) {
            throw new BusinessException("ProductKey不能为空");
        }
        if (CollectionUtils.isEmpty(verifyJobCreateRq.getTargetDeviceNameIn())) {
            throw new BusinessException("待验证的设备不能为空");
        }
        if (verifyJobCreateRq.getTargetDeviceNameIn().size() > 10) {
            throw new BusinessException("待验证的设备不能超过10个");
        }
        CreateOTAVerifyJobResponse response = null;
        try {
            response = otaUpgradeManager.createOTAVerifyJob(verifyJobCreateRq);
            if(!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null || response.getBody().getData() == null ) {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return response.getBody().getData().getJobId();
    }

    @Override
    public void reupgradeOTATask(OTAUpgradePackageReupgradeTaskRq reupgradeTaskRq, String operator) throws BusinessException {
        if (reupgradeTaskRq == null) {
            throw new BusinessException("重新升级指定批次下升级失败或升级取消的设备升级作业请求参数不能为空");
        }
        if (StringUtils.isBlank(reupgradeTaskRq.getJobId())) {
            throw new BusinessException("升级批次ID不能为空");
        }
        if (CollectionUtils.isEmpty(reupgradeTaskRq.getTaskIdIn())) {
            throw new BusinessException("设备升级作业ID不能为空");
        }
        if (reupgradeTaskRq.getTaskIdIn().size() > 10) {
            throw new BusinessException("TaskId个数范围为1~10个");
        }
        try {
            otaUpgradeManager.reupgradeOTATask(reupgradeTaskRq);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void confirmOTATask(OTAUpgradePackageConfirmTaskRq confirmTaskRq, String operator) throws BusinessException {
        if (confirmTaskRq == null) {
            throw new BusinessException("重新升级指定批次下升级失败或升级取消的设备升级作业请求参数不能为空");
        }
        if (CollectionUtils.isEmpty(confirmTaskRq.getTaskIdIn())) {
            throw new BusinessException("设备升级作业ID不能为空");
        }
        if (confirmTaskRq.getTaskIdIn().size() > 10) {
            throw new BusinessException("TaskId个数范围为1~10个");
        }
        try {
            ConfirmOTATaskResponse response = otaUpgradeManager.confirmOTATask(confirmTaskRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void cancelOTATaskByJob(OTAUpgradePackageCancelTaskByJobRq cancelTaskByJobRq, String operator) throws BusinessException {
        if (cancelTaskByJobRq == null) {
            throw new BusinessException("取消指定批次下的设备升级作业请求参数不能为空");
        }
        if (StringUtils.isBlank(cancelTaskByJobRq.getJobId())) {
            throw new BusinessException("升级批次ID不能为空");
        }
        try {
            CancelOTATaskByJobResponse response = otaUpgradeManager.cancelOTATaskByJob(cancelTaskByJobRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void cancelOTAStrategyByJob(OTAUpgradePackageCancelStrategyByJobRq cancelStrategyByJobRq, String operator) throws BusinessException {
        if (cancelStrategyByJobRq == null) {
            throw new BusinessException("取消静态任务请求参数不能为空");
        }
        if (StringUtils.isBlank(cancelStrategyByJobRq.getJobId())) {
            throw new BusinessException("升级批次ID不能为空");
        }
        try {
            CancelOTAStrategyByJobResponse response = otaUpgradeManager.cancelOTAStrategyByJob(cancelStrategyByJobRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public String createOTADynamicUpgradeJob(OTAUpgradePackageDynamicUpgradeJobCreateRq dynamicUpgradeJobCreateRq, String operator) throws BusinessException {
        if (dynamicUpgradeJobCreateRq == null) {
            throw new BusinessException("创建动态升级批次参数不能为空");
        }
        if (StringUtils.isBlank(dynamicUpgradeJobCreateRq.getFirmwareId())) {
            throw new BusinessException("升级包ID不能为空");
        }
        if (StringUtils.isBlank(dynamicUpgradeJobCreateRq.getProductKey())) {
            throw new BusinessException("产品的ProductKey不能为空");
        }
        if (CollectionUtils.isNotEmpty(dynamicUpgradeJobCreateRq.getTagList()) && dynamicUpgradeJobCreateRq.getTagList().size() > 10) {
            throw new BusinessException("最多添加10个批次标签");
        }
        CreateOTADynamicUpgradeJobResponse response = null;
        try {
            response = otaUpgradeManager.createOTADynamicUpgradeJob(dynamicUpgradeJobCreateRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null || response.getBody() == null || response.getBody().getData() == null) {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return response.getBody().getData().getJobId();
    }

    @Override
    public String createOTAStaticUpgradeJob(OTAUpgradePackageStaticUpgradeJobCreateRq staticUpgradeJobCreateRq, String operator) throws BusinessException {
        if (staticUpgradeJobCreateRq == null) {
            throw new BusinessException("创建静态升级批次参数不能为空");
        }
        if (StringUtils.isBlank(staticUpgradeJobCreateRq.getFirmwareId())) {
            throw new BusinessException("升级包ID不能为空");
        }
        if (StringUtils.isBlank(staticUpgradeJobCreateRq.getProductKey())) {
            throw new BusinessException("产品的ProductKey不能为空");
        }
        if (staticUpgradeJobCreateRq.getTargetSelection() == null) {
            throw new BusinessException("升级范围不能为空");
        }
        if (CollectionUtils.isNotEmpty(staticUpgradeJobCreateRq.getTagList()) && staticUpgradeJobCreateRq.getTagList().size() > 10) {
            throw new BusinessException("最多添加10个批次标签");
        }
        CreateOTAStaticUpgradeJobResponse response = null;
        try {
            if (StringUtils.isNotBlank(staticUpgradeJobCreateRq.getFilePath())){
                staticUpgradeJobCreateRq.setTargetDeviceNameIn(equipmentNameImporter.doImport(null,staticUpgradeJobCreateRq.getFilePath()));
            }
            response = otaUpgradeManager.createOTAStaticUpgradeJob(staticUpgradeJobCreateRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response == null || response.getBody() == null || response.getBody().getData() == null) {
                return null;
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return response.getBody().getData().getJobId();
    }

    @Override
    public OTAUpgradePackageGenerateUrlInfoDTO generateOTAUploadURL(OTAUpgradePackageGenerateUrlRq generateUrlRq, String operator) throws BusinessException {
        OTAUpgradePackageGenerateUrlInfoDTO urlInfoDTO=new OTAUpgradePackageGenerateUrlInfoDTO();
        try {
            GenerateOTAUploadURLResponse response = otaUpgradeManager.generateOTAUploadURL(generateUrlRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response==null||response.getBody()==null||response.getBody().getData()==null){
                return urlInfoDTO;
            }
            urlInfoDTO.setUrl(response.getBody().getData().getFirmwareUrl());
            urlInfoDTO.setHost(response.getBody().getData().getHost());
            urlInfoDTO.setKey(response.getBody().getData().getKey());
            urlInfoDTO.setAccessKeyId(response.getBody().getData().getOSSAccessKeyId());
            urlInfoDTO.setObjectStorage(response.getBody().getData().getObjectStorage());
            urlInfoDTO.setPolicy(response.getBody().getData().getPolicy());
            urlInfoDTO.setSignature(response.getBody().getData().getSignature());
            urlInfoDTO.setCreated(DateUtils.dealDateFormatUTC(response.getBody().getData().getUtcCreate()));
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return urlInfoDTO;
    }

    @Override
    public OTAUpgradePackageGenerateUrlInfoDTO generateDeviceNameListURL(OTAUpgradePackageGenerateDeviceNameListUrlRq generateDeviceNameListUrlRq, String operator) throws BusinessException {
        OTAUpgradePackageGenerateUrlInfoDTO urlInfoDTO=new OTAUpgradePackageGenerateUrlInfoDTO();
        try {
            GenerateDeviceNameListURLResponse response = otaUpgradeManager.generateDeviceNameListURL(generateDeviceNameListUrlRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException(response.getBody().getErrorMessage());
            }
            if (response==null||response.getBody()==null||response.getBody().getData()==null){
                return urlInfoDTO;
            }
            urlInfoDTO.setUrl(response.getBody().getData().getFileUrl());
            urlInfoDTO.setHost(response.getBody().getData().getHost());
            urlInfoDTO.setKey(response.getBody().getData().getKey());
            urlInfoDTO.setAccessKeyId(response.getBody().getData().getAccessKeyId());
            urlInfoDTO.setObjectStorage(response.getBody().getData().getObjectStorage());
            urlInfoDTO.setPolicy(response.getBody().getData().getPolicy());
            urlInfoDTO.setSignature(response.getBody().getData().getSignature());
            urlInfoDTO.setCreated(DateUtils.dealDateFormatUTC(response.getBody().getData().getUtcCreate()));
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return urlInfoDTO;
    }
}


