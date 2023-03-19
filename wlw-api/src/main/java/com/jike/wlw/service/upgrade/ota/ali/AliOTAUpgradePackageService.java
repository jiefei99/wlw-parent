package com.jike.wlw.service.upgrade.ota.ali;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
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
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageReupgradeTaskRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStaticUpgradeJobCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageTackByJobFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageVerifyJobCreateRq;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageGenerateUrlInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageInfoDTO;
import com.jike.wlw.service.upgrade.ota.dto.OTAUpgradePackageListDeviceTaskByJobDTO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchInfoVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageJobBatchListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageListVO;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradePackageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "阿里OTA升级-升级包")
public interface AliOTAUpgradePackageService {

    @ApiOperation(value = "获取OTA升级包列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OTAUpgradePackageListVO> query(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId", required = false) String tenantId,
                                                @ApiParam(required = true, value = "查询请求参数") @RequestBody OTAUpgradePackageFilter filter) throws BusinessException;

    @ApiOperation(value = "获取OTA升级包列表")
    @RequestMapping(value = "/queryEquipment", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OTAUpgradePackageListDeviceTaskByJobDTO> queryEquipment(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId", required = false) String tenantId,
                                                                         @ApiParam(required = true, value = "查询请求参数") @RequestBody OTAUpgradePackageTackByJobFilter filter) throws BusinessException;

    @ApiOperation(value = "获取OTA升级包列表")
    @RequestMapping(value = "/queryJobByFirmware", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<OTAUpgradePackageJobBatchListVO> queryJobByFirmware(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId", required = false) String tenantId,
                                                                     @ApiParam(required = true, value = "查询请求参数") @RequestBody OTAUpgradePackageJobByFirmwareFilter filter) throws BusinessException;

    @ApiOperation(value = "根据ID获取OTA升级包的详细信息")
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    OTAUpgradePackageInfoDTO getInfo(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                     @ApiParam(required = true, value = "OTA升级包ID") @RequestParam(value = "id") String id,
                                     @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "根据ID获取OTA升级包的详细信息")
    @RequestMapping(value = "/getBatchInfo", method = RequestMethod.GET)
    @ResponseBody
    OTAUpgradePackageJobBatchInfoVO getJobBatchInfo(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                                    @ApiParam(required = true, value = "升级批次ID") @RequestParam(value = "jobId") String jobId,
                                                    @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "根据ID获取OTA升级包的详细信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    OTAUpgradePackageVO get(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                            @ApiParam(required = true, value = "OTA升级包ID") @RequestParam(value = "id") String id,
                            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;


    @ApiOperation(value = "保存OTA升级包")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    void create(@ApiParam(required = false, value = "租户") @RequestParam(value = "tenantId", required = false) String tenantId,
                @ApiParam(required = true, value = "创建请求参数") @RequestBody OTAUpgradePackageCreateRq createRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "删除OTA升级包")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "删除请求参数") @RequestBody OTAUpgradePackageDeleteRq deleteRq,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "取消指定OTA升级包的设备升级")
    @RequestMapping(value = "/cancelOTATaskByDevice", method = RequestMethod.POST)
    @ResponseBody
    Boolean cancelOTATaskByDevice(@ApiParam(required = true, value = "删除请求参数") @RequestBody OTAUpgradePackageCancelTaskByDeviceRq cancelTaskByDeviceRq,
                                  @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "创建OTA升级包验证任务")
    @RequestMapping(value = "/createOTAVerifyJob", method = RequestMethod.POST)
    @ResponseBody
    String createOTAVerifyJob(@ApiParam(required = true, value = "验证请求参数") @RequestBody OTAUpgradePackageVerifyJobCreateRq verifyJobCreateRq,
                              @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "重新升级指定批次下升级失败或升级取消的设备升级作业")
    @RequestMapping(value = "/reupgradeOTATask", method = RequestMethod.POST)
    @ResponseBody
    void reupgradeOTATask(@ApiParam(required = true, value = "重新升级请求参数") @RequestBody OTAUpgradePackageReupgradeTaskRq reupgradeTaskRq,
                          @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "批量确认处于待确认状态的设备升级作业")
    @RequestMapping(value = "/confirmOTATask", method = RequestMethod.POST)
    @ResponseBody
    void confirmOTATask(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageConfirmTaskRq confirmTaskRq,
                        @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "取消指定批次下的设备升级作业")
    @RequestMapping(value = "/cancelOTATaskByJob", method = RequestMethod.POST)
    @ResponseBody
    void cancelOTATaskByJob(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageCancelTaskByJobRq cancelTaskByJobRq,
                            @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "取消动态升级策略")
    @RequestMapping(value = "/cancelOTAStrategyByJob", method = RequestMethod.POST)
    @ResponseBody
    void cancelOTAStrategyByJob(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageCancelStrategyByJobRq cancelStrategyByJobRq,
                                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "创建动态升级策略")
    @RequestMapping(value = "/createOTADynamicUpgradeJob", method = RequestMethod.POST)
    @ResponseBody
    String createOTADynamicUpgradeJob(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageDynamicUpgradeJobCreateRq dynamicUpgradeJobCreateRq,
                                      @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "创建静态升级策略")
    @RequestMapping(value = "/createOTAStaticUpgradeJob", method = RequestMethod.POST)
    @ResponseBody
    String createOTAStaticUpgradeJob(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageStaticUpgradeJobCreateRq staticUpgradeJobCreateRq,
                                     @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "生成升级包文件上传到OSS的URL及详细信息")
    @RequestMapping(value = "/generateOTAUploadURL", method = RequestMethod.POST)
    @ResponseBody
    OTAUpgradePackageGenerateUrlInfoDTO generateOTAUploadURL(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageGenerateUrlRq generateUrlRq,
                                                             @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation(value = "生成设备列表文件上传到OSS的URL及详细信息")
    @RequestMapping(value = "/generateDeviceNameListURL", method = RequestMethod.POST)
    @ResponseBody
    OTAUpgradePackageGenerateUrlInfoDTO generateDeviceNameListURL(@ApiParam(required = true, value = "请求参数") @RequestBody OTAUpgradePackageGenerateDeviceNameListUrlRq generateDeviceNameListUrlRq,
                                     @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

}
