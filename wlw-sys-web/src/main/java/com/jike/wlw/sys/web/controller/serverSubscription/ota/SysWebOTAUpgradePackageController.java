package com.jike.wlw.sys.web.controller.serverSubscription.ota;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.source.Source;
import com.jike.wlw.service.source.SourceTypes;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCancelTaskByDeviceRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDynamicUpgradeJobCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageGenerateUrlRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobByFirmwareFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageReupgradeTaskRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStaticUpgradeJobCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTackByJobFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageVerifyJobCreateRq;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageGenerateUrlInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageListDeviceTaskByJobDTO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageGenerateUrlInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListBatchDeviceTaskByJobVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListDeviceTaskByJobVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import com.jike.wlw.sys.web.config.fegin.AliOTAUpgradePackageFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.controller.source.SysWebSourceController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @title: SysWebOTAUpgradeController
 * @Author RS
 * @Date: 2023/3/8 16:43
 * @Version 1.0
 */
@Slf4j
@Api(value = "OTA升级包", tags = {"OTA升级"})
@RestController
@RequestMapping(value = "/web/OTAUpgradePackage/aliyun", produces = "application/json;charset=utf-8")
public class SysWebOTAUpgradePackageController extends BaseController {

    @Autowired
    private AliOTAUpgradePackageFeignClient aliOTAUpgradePackageFeignClient;
    @Autowired
    private SysWebSourceController sourceController;

    @ApiOperation(value = "根据查询条件查询OTA升级包")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageListVO>> query(@RequestBody OTAUpgradePackageFilter filter) throws BusinessException {
        try {
            if (filter.getPage() < 0) {
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize() < 0 || filter.getPageSize() > 100) {
                throw new BusinessException("每页显示数量0-100，请重新选择");
            }

            PagingResult<OTAUpgradePackageListVO> result = new PagingResult<>();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.query(getTenantId(), filter);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包设备列表")
    @RequestMapping(value = "/queryEquipment", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageListDeviceTaskByJobVO>> queryEquipment(@RequestBody OTAUpgradePackageTackByJobFilter filter) throws BusinessException {
        try {
            if (filter.getPage() < 0) {
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize() < 0 || filter.getPageSize() > 100) {
                throw new BusinessException("每页显示数量0-100，请重新选择");
            }
            List<OTAUpgradePackageListDeviceTaskByJobVO> list = new ArrayList<>();
            PagingResult<OTAUpgradePackageListDeviceTaskByJobVO> result = new PagingResult<>();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> resp = aliOTAUpgradePackageFeignClient.queryEquipment(getTenantId(), filter);
                result.setPage(resp.getPage());
                result.setPageCount(resp.getPageCount());
                result.setPageSize(resp.getPageSize());
                result.setTotal(resp.getTotal());
                if (resp.getData() != null && !CollectionUtils.isEmpty(resp.getData())) {
                    for (OTAUpgradePackageListDeviceTaskByJobDTO jobDTO : resp.getData()) {
                        OTAUpgradePackageListDeviceTaskByJobVO target = new OTAUpgradePackageListDeviceTaskByJobVO();
                        BeanUtils.copyProperties(jobDTO, target);
                        list.add(target);
                    }
                }
                result.setData(list);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包升级任务批次列表")
    @RequestMapping(value = "/queryJobByFirmware", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageJobBatchListVO>> queryJobByFirmware(@RequestBody OTAUpgradePackageJobByFirmwareFilter filter) throws BusinessException {
        try {
            if (filter.getPage() < 0) {
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize() < 0 || filter.getPageSize() > 200) {
                throw new BusinessException("每页显示数量0-200，请重新选择");
            }
            if (StringUtils.isBlank(filter.getFirmwareId())) {
                throw new BusinessException("升级包ID不能为空");
            }

            PagingResult<OTAUpgradePackageJobBatchListVO> result = new PagingResult<>();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.queryJobByFirmware(getTenantId(), filter);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA信息升级包")
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageInfoVO> getInfo(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                         @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId", required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageInfoVO result = new OTAUpgradePackageInfoVO();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                OTAUpgradePackageInfoDTO infoDTO = aliOTAUpgradePackageFeignClient.getInfo(getTenantId(), id, iotInstanceId);
                BeanUtils.copyProperties(infoDTO, result);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "刷新验证进度")
    @RequestMapping(value = "/getVerifyProgressInfo", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Integer> getVerifyProgressInfo(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                       @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId", required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            Integer result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                OTAUpgradePackageInfoDTO infoDTO = aliOTAUpgradePackageFeignClient.getInfo(getTenantId(), id, iotInstanceId);
                if (infoDTO != null) {
                    result = infoDTO.getVerifyProgress();
                }
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取上传url")
    @RequestMapping(value = "/generateOTAUploadURL", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<OTAUpgradePackageGenerateUrlInfoVO> generateOTAUploadURL(@ApiParam(required = true, value = "id") @RequestBody OTAUpgradePackageGenerateUrlRq generateUrlRq) throws Exception {
        try {
            OTAUpgradePackageGenerateUrlInfoVO result = new OTAUpgradePackageGenerateUrlInfoVO();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                OTAUpgradePackageGenerateUrlInfoDTO urlInfoDTO = aliOTAUpgradePackageFeignClient.generateOTAUploadURL(generateUrlRq, getUserName());
                BeanUtils.copyProperties(urlInfoDTO, result);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询各个状态下的设备升级数")
    @RequestMapping(value = "/getBatchDeviceTaskByJobNum", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageListBatchDeviceTaskByJobVO> getBatchDeviceTaskByJobNum(@ApiParam(required = true, value = "jobId") @RequestParam(value = "jobId") String jobId,
                                                                                                @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId", required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(jobId)) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            //可以改成跟queryDeviceGroupList一样使用递归
            OTAUpgradePackageTackByJobFilter filter = new OTAUpgradePackageTackByJobFilter();
            filter.setJobId(jobId);
            filter.setPage(1);
            filter.setPageSize(100);
            OTAUpgradePackageListBatchDeviceTaskByJobVO vo = new OTAUpgradePackageListBatchDeviceTaskByJobVO();

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> result = aliOTAUpgradePackageFeignClient.queryEquipment(getTenantId(), filter);
                if (result == null || result.getData() == null || result.getTotal() == 0 || CollectionUtils.isEmpty(result.getData())) {
                    return ActionResult.ok(new OTAUpgradePackageListBatchDeviceTaskByJobVO());
                }
                List<OTAUpgradePackageListDeviceTaskByJobDTO> batchDeviceList = new ArrayList<>();
                batchDeviceList.addAll(result.getData());
                Long forCount = (result.getTotal() + 100 - 1) / 100; //总数不止一页的情况
                for (int i = 0; i < forCount - 1; i++) {
                    filter.setPage(filter.getPage() + 1);
                    result = aliOTAUpgradePackageFeignClient.queryEquipment(getTenantId(), filter);
                    if (result == null || result.getData() == null || result.getTotal() == 0 || CollectionUtils.isEmpty(result.getData())) {
                        continue;
                    }
                    batchDeviceList.addAll(result.getData());
                }
                Map<OTAUpgradePackageTaskStatusType, Long> statusTypeCountMap = batchDeviceList.parallelStream().map(OTAUpgradePackageListDeviceTaskByJobDTO::getTaskStatus).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                vo.setTotal(Long.valueOf(batchDeviceList.size()));
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CONFIRM) != null) {
                    vo.setConfirmTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CONFIRM));
                }
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.FAILED) != null) {
                    vo.setFailedTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.FAILED));
                }
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.QUEUED) != null) {
                    vo.setQueuedTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.QUEUED));
                }
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.NOTIFIED) != null) {
                    vo.setNotifiedTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.NOTIFIED));
                }
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.SUCCEEDED) != null) {
                    vo.setSuccessTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.SUCCEEDED));
                }
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CANCELED) != null) {
                    vo.setCanceledTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CANCELED));
                }
                if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.IN_PROGRESS) != null) {
                    vo.setUpgradingTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.IN_PROGRESS));
                }
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(vo);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包批次信息详情")
    @RequestMapping(value = "/getJobBatchInfo", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageJobBatchInfoVO> getJobBatchInfo(@ApiParam(required = true, value = "jobId") @RequestParam(value = "jobId") String jobId,
                                                                         @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId", required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(jobId)) {
            throw new BusinessException("升级批次ID不能为空");
        }
        try {
            OTAUpgradePackageJobBatchInfoVO result = new OTAUpgradePackageJobBatchInfoVO();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.getJobBatchInfo(getTenantId(), jobId, iotInstanceId);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageVO> get(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                 @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId", required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageVO result = new OTAUpgradePackageVO();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.get(getTenantId(), id, iotInstanceId);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建OTA升级包")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> create(@RequestBody OTAUpgradePackageCreateRq createRq) throws BusinessException {
        if (StringUtils.isBlank(createRq.getDestVersion())) {
            throw new BusinessException("当前OTA升级包版本号不能为空");
        }
        if (StringUtils.isBlank(createRq.getFirmwareName())) {
            throw new BusinessException("升级包名称不能为空");
        }
        try {
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliOTAUpgradePackageFeignClient.create(getTenantId(), createRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除OTA升级包")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(@RequestBody OTAUpgradePackageDeleteRq deleteRq) throws BusinessException {
        try {
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliOTAUpgradePackageFeignClient.delete(deleteRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "取消指定OTA升级包下状态为待推送、已推送、升级中状态的设备升级作业")
    @RequestMapping(value = "/cancelOTATaskByDevice", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Boolean> cancelOTATaskByDevice(@RequestBody OTAUpgradePackageCancelTaskByDeviceRq cancelTaskByDeviceRq) throws BusinessException {
        try {
            Boolean result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.cancelOTATaskByDevice(cancelTaskByDeviceRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "重新发起升级失败或升级取消的设备升级作业")
    @RequestMapping(value = "/reUpgradeOTATaskByDevice", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> reUpgradeOTATaskByDevice(@RequestBody OTAUpgradePackageReupgradeTaskRq reupgradeTaskRq) throws BusinessException {
        try {
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliOTAUpgradePackageFeignClient.reupgradeOTATask(reupgradeTaskRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "OTA升级包校验")
    @RequestMapping(value = "/createOTAVerifyJob", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> createOTAVerifyJob(@RequestBody OTAUpgradePackageVerifyJobCreateRq verifyJobCreateRq) throws BusinessException {
        try {
            String result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.createOTAVerifyJob(verifyJobCreateRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "动态升级")
    @RequestMapping(value = "/createOTADynamicUpgradeJob", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> createOTADynamicUpgradeJob(@RequestBody OTAUpgradePackageDynamicUpgradeJobCreateRq dynamicUpgradeJobCreateRq) throws BusinessException {
        try {
            String result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.createOTADynamicUpgradeJob(dynamicUpgradeJobCreateRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "静态升级")
    @RequestMapping(value = "/createOTAStaticUpgradeJob", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> createOTAStaticUpgradeJob(@RequestBody OTAUpgradePackageStaticUpgradeJobCreateRq staticUpgradeJobCreateRq) throws BusinessException {
        try {
            String result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliOTAUpgradePackageFeignClient.createOTAStaticUpgradeJob(staticUpgradeJobCreateRq, getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "上传升级包")
    @RequestMapping(value = "/uploadUpgrade", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Map<String, String>> uploadUpgrade(@RequestParam("multipartFile") MultipartFile multipartFile) throws BusinessException {
        if (multipartFile == null) {
            throw new BusinessException("升级包不能为空");
        }
        Map<String, String> uploadMap = new HashMap<>();
        try {
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                OTAUpgradePackageGenerateUrlInfoDTO urlInfoDTO = aliOTAUpgradePackageFeignClient.generateOTAUploadURL(new OTAUpgradePackageGenerateUrlRq(), getUserName());
                File file = multipartFileToFile(multipartFile);
                String strFile = fileToString(file);
                postObject(urlInfoDTO.getKey(), urlInfoDTO.getHost(), urlInfoDTO.getPolicy(), urlInfoDTO.getAccessKeyId(), urlInfoDTO.getSignature(), strFile);
                uploadMap.put(file.getName(), urlInfoDTO.getUrl());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
        } catch (Exception e) {
            return dealWithError(e);
        }
        return ActionResult.ok(uploadMap);
    }

    public static boolean postObject(String key,
                                     String host,
                                     String policy,
                                     String ossAccessKeyId,
                                     String signature,
                                     String data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(host);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("key", key, ContentType.TEXT_PLAIN);
        builder.addTextBody("policy", policy, ContentType.TEXT_PLAIN);
        builder.addTextBody("OSSAccessKeyId", ossAccessKeyId, ContentType.TEXT_PLAIN);
        builder.addTextBody("signature", signature, ContentType.TEXT_PLAIN);
        builder.addTextBody("success_action_status", "200", ContentType.TEXT_PLAIN);
        builder.addBinaryBody("file", data.getBytes());

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);

        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        }

        return false;
    }

    /**
     * 将MultipartFile转换为File
     *
     * @param multiFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            log.error("MultipartFile转File失败", e);
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String fileToString(File file) throws IOException {
        if (file.exists()) {
            byte[] data = new byte[(int) file.length()];
            boolean result;
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                int len = inputStream.read(data);
                result = len == data.length;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            if (result) {
                return new String(data);
            }
        }
        return null;
    }
}


