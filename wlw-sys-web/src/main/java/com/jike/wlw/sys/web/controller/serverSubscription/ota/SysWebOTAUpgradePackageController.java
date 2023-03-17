package com.jike.wlw.sys.web.controller.serverSubscription.ota;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCancelTaskByDeviceRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageJobByFirmwareFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageReupgradeTaskRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTackByJobFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTaskStatusType;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageListDeviceTaskByJobDTO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListBatchDeviceTaskByJobVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListDeviceTaskByJobVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import com.jike.wlw.sys.web.config.fegin.AliOTAUpgradePackageFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
@RequestMapping(value = "/web/OTAUpgradePackage", produces = "application/json;charset=utf-8")
public class SysWebOTAUpgradePackageController extends BaseController {

    @Autowired
    private AliOTAUpgradePackageFeignClient aliOTAUpgradePackageFeignClient;

    @ApiOperation(value = "根据查询条件查询OTA升级包")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageListVO>> query(@RequestBody OTAUpgradePackageFilter filter) throws BusinessException {
        try {
            if (filter.getPage()<0){
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize()<0||filter.getPageSize()>100){
                throw new BusinessException("每页显示数量0-100，请重新选择");
            }
            PagingResult<OTAUpgradePackageListVO> query = aliOTAUpgradePackageFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(query);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包设备列表")
    @RequestMapping(value = "/queryEquipment", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageListDeviceTaskByJobVO>> queryEquipment(@RequestBody OTAUpgradePackageTackByJobFilter filter) throws BusinessException {
        try {
            if (filter.getPage()<0){
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize()<0||filter.getPageSize()>100){
                throw new BusinessException("每页显示数量0-100，请重新选择");
            }
            List<OTAUpgradePackageListDeviceTaskByJobVO> list=new ArrayList<>();
            PagingResult<OTAUpgradePackageListDeviceTaskByJobVO> resultVO=new PagingResult<>();
            PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> resp = aliOTAUpgradePackageFeignClient.queryEquipment(getTenantId(), filter);
            resultVO.setPage(resp.getPage());
            resultVO.setPageCount(resp.getPageCount());
            resultVO.setPageSize(resp.getPageSize());
            resultVO.setTotal(resp.getTotal());
            if (resp.getData()!=null&&!CollectionUtils.isEmpty(resp.getData())){
                for (OTAUpgradePackageListDeviceTaskByJobDTO source : resp.getData()) {
                    OTAUpgradePackageListDeviceTaskByJobVO target=new OTAUpgradePackageListDeviceTaskByJobVO();
                    BeanUtils.copyProperties(source,target);
                    list.add(target);
                }
            }
            resultVO.setData(list);
            return ActionResult.ok(resultVO);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包升级任务批次列表")
    @RequestMapping(value = "/queryJobByFirmware", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<OTAUpgradePackageJobBatchListVO>> queryJobByFirmware(@RequestBody OTAUpgradePackageJobByFirmwareFilter filter) throws BusinessException {
        try {
            if (filter.getPage()<0){
                throw new BusinessException("页数从1开始排序，请重新选择");
            }
            if (filter.getPageSize()<0||filter.getPageSize()>200){
                throw new BusinessException("每页显示数量0-200，请重新选择");
            }
            if (StringUtils.isBlank(filter.getFirmwareId())){
                throw new BusinessException("升级包ID不能为空");
            }
            PagingResult<OTAUpgradePackageJobBatchListVO> result = aliOTAUpgradePackageFeignClient.queryJobByFirmware(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA信息升级包")
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageInfoVO> getInfo(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                         @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)){
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageInfoDTO infoDTO = aliOTAUpgradePackageFeignClient.getInfo(getTenantId(), id, iotInstanceId);
            OTAUpgradePackageInfoVO target=new OTAUpgradePackageInfoVO();
            BeanUtils.copyProperties(infoDTO,target);
            return ActionResult.ok(target);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "刷新验证进度")
    @RequestMapping(value = "/getVerifyProgressInfo", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Integer> getVerifyProgressInfo(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                         @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)){
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageInfoDTO infoDTO = aliOTAUpgradePackageFeignClient.getInfo(getTenantId(), id, iotInstanceId);
            return ActionResult.ok(infoDTO.getVerifyProgress());
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询各个状态下的设备升级数")
    @RequestMapping(value = "/getBatchDeviceTaskByJobNum", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageListBatchDeviceTaskByJobVO> getBatchDeviceTaskByJobNum(@ApiParam(required = true, value = "jobId") @RequestParam(value = "jobId") String jobId,
                                                                                                @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(jobId)){
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageTackByJobFilter filter=new OTAUpgradePackageTackByJobFilter();
            filter.setJobId(jobId);
            filter.setPage(1);
            filter.setPageSize(100);
            PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> result = aliOTAUpgradePackageFeignClient.queryEquipment(getTenantId(), filter);
            if (result==null||result.getData()==null||result.getTotal()==0||CollectionUtils.isEmpty(result.getData())){
                return ActionResult.ok(new OTAUpgradePackageListBatchDeviceTaskByJobVO());
            }
            List<OTAUpgradePackageListDeviceTaskByJobDTO> batchDeviceList = new ArrayList<>();
            batchDeviceList.addAll(result.getData());
            Long forCount = (result.getTotal() + 100 - 1) / 100; //总数不止一页的情况
            for (int i = 0; i < forCount - 1; i++) {
                filter.setPage(filter.getPage() + 1);
                result = aliOTAUpgradePackageFeignClient.queryEquipment(getTenantId(), filter);
                if (result==null||result.getData()==null||result.getTotal()==0||CollectionUtils.isEmpty(result.getData())){
                    continue;
                }
                batchDeviceList.addAll(result.getData());
            }
            Map<OTAUpgradePackageTaskStatusType, Long> statusTypeCountMap = batchDeviceList.parallelStream().map(OTAUpgradePackageListDeviceTaskByJobDTO::getTaskStatus).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            OTAUpgradePackageListBatchDeviceTaskByJobVO vo=new OTAUpgradePackageListBatchDeviceTaskByJobVO();
            vo.setTotal(Long.valueOf(batchDeviceList.size()));
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CONFIRM)!=null){
                vo.setConfirmTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CONFIRM));
            }
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.FAILED)!=null){
                vo.setFailedTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.FAILED));
            }
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.QUEUED)!=null){
                vo.setQueuedTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.QUEUED));
            }
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.NOTIFIED)!=null){
                vo.setNotifiedTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.NOTIFIED));
            }
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.SUCCEEDED)!=null){
                vo.setSuccessTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.SUCCEEDED));
            }
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CANCELED)!=null){
                vo.setCanceledTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.CANCELED));
            }
            if (statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.IN_PROGRESS)!=null){
                vo.setUpgradingTotal(statusTypeCountMap.get(OTAUpgradePackageTaskStatusType.IN_PROGRESS));
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
                                                                         @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(jobId)){
            throw new BusinessException("升级批次ID不能为空");
        }
        try {
            OTAUpgradePackageJobBatchInfoVO otaUpgradePackageJobBatchInfoVO = aliOTAUpgradePackageFeignClient.getJobBatchInfo(getTenantId(), jobId, iotInstanceId);
            return ActionResult.ok(otaUpgradePackageJobBatchInfoVO);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询OTA升级包")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<OTAUpgradePackageVO> get(@ApiParam(required = true, value = "id") @RequestParam(value = "id") String id,
                                                 @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(id)){
            throw new BusinessException("OTA升级包ID不能为空");
        }
        try {
            OTAUpgradePackageVO otaUpgradePackageVO = aliOTAUpgradePackageFeignClient.get(getTenantId(), id, iotInstanceId);
            return ActionResult.ok(otaUpgradePackageVO);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建OTA升级包")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> create(@RequestBody OTAUpgradePackageCreateRq createRq) throws BusinessException {
        if (StringUtils.isBlank(createRq.getDestVersion())){
            throw new BusinessException("当前OTA升级包版本号不能为空");
        }
        if (StringUtils.isBlank(createRq.getFirmwareName())){
            throw new BusinessException("升级包名称不能为空");
        }
        try {
            aliOTAUpgradePackageFeignClient.create(getTenantId(), createRq, getUserName());
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
            aliOTAUpgradePackageFeignClient.delete(deleteRq, getUserName());
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
            Boolean flag = aliOTAUpgradePackageFeignClient.cancelOTATaskByDevice(cancelTaskByDeviceRq, getUserName());
            return ActionResult.ok(flag);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
    @ApiOperation(value = "重新发起升级失败或升级取消的设备升级作业")
    @RequestMapping(value = "/reUpgradeOTATaskByDevice", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> reUpgradeOTATaskByDevice(@RequestBody OTAUpgradePackageReupgradeTaskRq reupgradeTaskRq) throws BusinessException {
        try {
            aliOTAUpgradePackageFeignClient.reupgradeOTATask(reupgradeTaskRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}


