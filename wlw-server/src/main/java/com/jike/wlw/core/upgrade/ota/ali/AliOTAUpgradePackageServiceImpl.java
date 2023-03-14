package com.jike.wlw.core.upgrade.ota.ali;

import com.aliyun.iot20180120.models.CancelOTATaskByDeviceResponse;
import com.aliyun.iot20180120.models.ListOTAFirmwareResponse;
import com.aliyun.iot20180120.models.ListOTAFirmwareResponseBody.ListOTAFirmwareResponseBodyFirmwareInfoSimpleFirmwareInfo;
import com.aliyun.iot20180120.models.ListOTAJobByFirmwareResponse;
import com.aliyun.iot20180120.models.ListOTAJobByFirmwareResponseBody.ListOTAJobByFirmwareResponseBodyDataSimpleOTAJobInfo;
import com.aliyun.iot20180120.models.ListOTATaskByJobResponse;
import com.aliyun.iot20180120.models.ListOTATaskByJobResponseBody;
import com.aliyun.iot20180120.models.ListOTATaskByJobResponseBody.ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo;
import com.aliyun.iot20180120.models.QueryOTAFirmwareResponse;
import com.aliyun.iot20180120.models.QueryOTAFirmwareResponseBody.QueryOTAFirmwareResponseBodyFirmwareInfoMultiFiles;
import com.aliyun.iot20180120.models.QueryOTAJobResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.DateUtils;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.upgrade.iot.OTAUpgradeManager;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCancelTaskByDeviceRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobByFirmwareFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobSelectionType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobTargetSelectionType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTackByJobFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageType;
import com.jike.wlw.service.upgrade.ota.ali.AliOTAUpgradePackageService;
import com.jike.wlw.service.upgrade.ota.dto.OTAFirmwareInfoMultiFilesDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageListDeviceTaskByJobDTO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
//todo 这些其实应该用DTO类，不能直接使用VO的
public class AliOTAUpgradePackageServiceImpl extends BaseService implements AliOTAUpgradePackageService {

    @Autowired
    private OTAUpgradeManager otaUpgradeManager;

    @Override
    public PagingResult<OTAUpgradePackageListVO> query(String tenantId, OTAUpgradePackageFilter filter) throws BusinessException {
        List<OTAUpgradePackageListVO> otaUpgradePackageVOList = new ArrayList<>();
        int total = 0;
        try {
            ListOTAFirmwareResponse response = otaUpgradeManager.listOTAFirmware(filter);
            if (response.getBody() == null || response.getBody() == null || !response.getBody().getSuccess() ||
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
        return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, otaUpgradePackageVOList);
    }

    @Override
    public PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> queryEquipment(String tenantId, OTAUpgradePackageTackByJobFilter filter) throws BusinessException {
        if (filter == null || StringUtils.isBlank(filter.getJobId())) {
            throw new BusinessException("升级批次ID不能为空");
        }
        List<OTAUpgradePackageListDeviceTaskByJobDTO> list = new ArrayList<>();
        try {
            ListOTATaskByJobResponse response = otaUpgradeManager.listOTATaskByJob(filter);
            if (response == null || response.getBody() == null || !response.getBody().getSuccess() || response.getBody().getData() == null || CollectionUtils.isEmpty(response.getBody().getData().getSimpleOTATaskInfo())) {
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
            if (response == null || response.getBody() == null || !response.getBody().getSuccess() || response.getBody().getData() == null || CollectionUtils.isEmpty(response.getBody().getData().getSimpleOTAJobInfo())) {
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
            if (response == null || !response.getBody().getSuccess() || response.getBody().getFirmwareInfo() == null) {
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
            if (response == null || !response.getBody().getSuccess() || response.getBody().getData() == null) {
                return otaUpgradePackageJobBatchInfoVO;
            }
            BeanUtils.copyProperties(response.getBody().getData(), otaUpgradePackageJobBatchInfoVO);
            if (StringUtils.isNotBlank(response.getBody().getData().getJobStatus())) {
                otaUpgradePackageJobBatchInfoVO.setJobStatusType(OTAUpgradePackageJobStatusType.valueOf(response.getBody().getData().getJobStatus()));
            }
            if (StringUtils.isNotBlank(response.getBody().getData().getJobStatus())) {
                otaUpgradePackageJobBatchInfoVO.setTargetSelectionType(OTAUpgradePackageJobTargetSelectionType.valueOf(response.getBody().getData().getTargetSelection()));
            }
            otaUpgradePackageJobBatchInfoVO.setUtcStartDate(DateUtils.dealDateFormatUTC(response.getBody().getData().getUtcStartTime()));
            if (response.getBody().getData().getSrcVersions() != null) {
                otaUpgradePackageJobBatchInfoVO.setSrcVersionList(response.getBody().getData().getSrcVersions().getSrcVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otaUpgradePackageJobBatchInfoVO;
    }

    //todo 后期可以根据Api方法进行拆分
    @Override
    public OTAUpgradePackageVO get(String tenantId, String id, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("当前OTA升级包ID不能为空");
        }
        OTAUpgradePackageVO otaUpgradePackageVO = new OTAUpgradePackageVO();
        try {
            // 先查询OTA升级包的基本信息
            QueryOTAFirmwareResponse response = otaUpgradeManager.queryOTAFirmware(id, iotInstanceId);
            if (response == null || !response.getBody().getSuccess() || response.getBody().getFirmwareInfo() == null) {
                throw new BusinessException("找不到此OTA升级包");
            }
            BeanUtils.copyProperties(response.getBody().getFirmwareInfo(), otaUpgradePackageVO);
            if (response.getBody().getFirmwareInfo().getType() != null) {
                otaUpgradePackageVO.setType(OTAUpgradePackageType.map.get(response.getBody().getFirmwareInfo().getType()));
            }
            //计算目标设备数量
            OTAUpgradePackageTackByJobFilter filter = new OTAUpgradePackageTackByJobFilter();
            filter.setJobId(id);
            filter.setIotInstanceId(iotInstanceId);
            filter.setPage(1);
            filter.setPageSize(100);
            ListOTATaskByJobResponse listOTATaskByJobResponse = otaUpgradeManager.listOTATaskByJob(filter);
            if (!listOTATaskByJobResponse.getBody().getSuccess() || listOTATaskByJobResponse.getBody().getData() == null || CollectionUtils.isEmpty(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo())) {
                return otaUpgradePackageVO;
            }
            List<ListOTATaskByJobResponseBody.ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo> taskInfoList = new ArrayList<>();
            taskInfoList.addAll(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo());
            int forCount = (listOTATaskByJobResponse.getBody().getTotal() + 100 - 1) / 100; //总数不止一页的情况
            for (int i = 0; i < forCount - 1; i++) {
                filter.setPage(filter.getPage() + 1);
                listOTATaskByJobResponse = otaUpgradeManager.listOTATaskByJob(filter);
                if (!listOTATaskByJobResponse.getBody().getSuccess() || listOTATaskByJobResponse.getBody().getData() == null || CollectionUtils.isEmpty(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo())) {
                    continue;
                }
                taskInfoList.addAll(listOTATaskByJobResponse.getBody().getData().getSimpleOTATaskInfo());
            }
            int targetDeviceUpgradeTotal = 0;
            int targetSuccessTotal = 0;
            int targetFailTotal = 0;
            int targetCancelTotal = 0;
            for (ListOTATaskByJobResponseBody.ListOTATaskByJobResponseBodyDataSimpleOTATaskInfo listOTATaskByJobResponseBodyDataSimpleOTATaskInfo : taskInfoList) {
                if ("SUCCEEDED".equals(listOTATaskByJobResponseBodyDataSimpleOTATaskInfo.getTaskStatus())) {
                    targetSuccessTotal++;
                }
                if ("FAILED".equals(listOTATaskByJobResponseBodyDataSimpleOTATaskInfo.getTaskStatus())) {
                    targetFailTotal++;
                }
                if ("CANCELED".equals(listOTATaskByJobResponseBodyDataSimpleOTATaskInfo.getTaskStatus())) {
                    targetCancelTotal++;
                }
                targetDeviceUpgradeTotal++;
            }
            otaUpgradePackageVO.setTargetDeviceUpgradeTotal(targetDeviceUpgradeTotal);
            otaUpgradePackageVO.setTargetCancelTotal(targetCancelTotal);
            otaUpgradePackageVO.setTargetSuccessTotal(targetSuccessTotal);
            otaUpgradePackageVO.setTargetFailTotal(targetFailTotal);
            return otaUpgradePackageVO;
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
            otaUpgradeManager.createOTAFirmware(createRq);
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
            otaUpgradeManager.deleteOTAFirmware(deleteRq.getFirmwareId(), deleteRq.getIotInstanceId());
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
            if (resp == null || resp.getBody() == null || resp.getBody().getSuccess() == null) {
                return false;
            }
            return resp.getBody().getSuccess();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

}

