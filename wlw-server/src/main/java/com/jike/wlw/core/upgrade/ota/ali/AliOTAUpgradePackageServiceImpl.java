package com.jike.wlw.core.upgrade.ota.ali;

import com.aliyun.iot20180120.models.ListOTAFirmwareResponse;
import com.aliyun.iot20180120.models.ListOTAFirmwareResponseBody.ListOTAFirmwareResponseBodyFirmwareInfoSimpleFirmwareInfo;
import com.aliyun.iot20180120.models.QueryOTAFirmwareResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.DateUtils;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.upgrade.iot.OTAUpgradeManager;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStatusType;
import com.jike.wlw.service.upgrade.ota.ali.AliOTAUpgradePackageService;
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
                otaUpgradePackageListVO.setStatus(OTAUpgradePackageStatusType.map.get(firmwareInfo.getStatus()));
                otaUpgradePackageListVO.setCreated(DateUtils.dealDateFormatUTC(firmwareInfo.getUtcCreate()));
                otaUpgradePackageVOList.add(otaUpgradePackageListVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, otaUpgradePackageVOList);
    }

    @Override
    public OTAUpgradePackageVO get(String tenantId, String id, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(id)){
            throw new BusinessException("当前OTA升级包ID不能为空");
        }
        OTAUpgradePackageVO otaUpgradePackageVO=new OTAUpgradePackageVO();
        try {
            QueryOTAFirmwareResponse response = otaUpgradeManager.queryOTAFirmware(id, iotInstanceId);
            if (response==null||!response.getBody().getSuccess()||response.getBody().getFirmwareInfo()==null){
                return otaUpgradePackageVO;
            }
            BeanUtils.copyProperties(response.getBody().getFirmwareInfo(),otaUpgradePackageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otaUpgradePackageVO;
    }

    @Override
    public void create(String tenantId, OTAUpgradePackageCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(createRq.getDestVersion())){
            throw new BusinessException("当前OTA升级包版本号不能为空");
        }
        if (StringUtils.isBlank(createRq.getFirmwareName())){
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
        if (StringUtils.isBlank(deleteRq.getFirmwareId())){
            throw new BusinessException("升级包id不能为空");
        }
        try {
            otaUpgradeManager.deleteOTAFirmware(deleteRq.getFirmwareId(),deleteRq.getIotInstanceId());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

//    @Override
//    public void getOTAUploadUrl(String operator) throws BusinessException {
//
//    }
}


