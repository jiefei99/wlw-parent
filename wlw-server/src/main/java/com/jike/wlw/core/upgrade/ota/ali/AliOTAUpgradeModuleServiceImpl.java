package com.jike.wlw.core.upgrade.ota.ali;

import com.aliyun.iot20180120.models.ListOTAModuleByProductResponse;
import com.aliyun.iot20180120.models.ListOTAModuleByProductResponseBody.ListOTAModuleByProductResponseBodyData;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.DateUtils;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.upgrade.iot.OTAUpgradeManager;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleCreateRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleDeleteRq;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleFilter;
import com.jike.wlw.service.upgrade.ota.OTAUpgradeModuleUpdateRq;
import com.jike.wlw.service.upgrade.ota.ali.AliOTAUpgradeModuleService;
import com.jike.wlw.service.upgrade.ota.vo.OTAUpgradeModuleVO;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: AliOTAUpgradeModuleServiceImpl
 * @Author RS
 * @Date: 2023/3/9 16:02
 * @Version 1.0
 */
@Slf4j
@RestController("OTAUpgradeModuleServiceAliImpl")
@ApiModel("阿里OTA升级模块实现")
@RequestMapping(value = "service/aliOTAUpgradeModule", produces = "application/json;charset=utf-8")
public class AliOTAUpgradeModuleServiceImpl extends BaseService implements AliOTAUpgradeModuleService {

    @Autowired
    private OTAUpgradeManager otaUpgradeManager;

    @Override
    public PagingResult<OTAUpgradeModuleVO> query(OTAUpgradeModuleFilter filter) throws BusinessException {
        int total=0;
        List<OTAUpgradeModuleVO> otaUpgradeModuleVOListt=new ArrayList<>();
        try {
            ListOTAModuleByProductResponse response = otaUpgradeManager.listOTAModuleByProduct(filter);
            if (response==null||response.getBody()==null||!response.getBody().getSuccess()||
                    CollectionUtils.isEmpty(response.getBody().getData())){
                return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, otaUpgradeModuleVOListt);
            }
            for (ListOTAModuleByProductResponseBodyData info : response.getBody().getData()) {
                OTAUpgradeModuleVO otaUpgradeModuleVO=new OTAUpgradeModuleVO();
                BeanUtils.copyProperties(info,otaUpgradeModuleVO);
                otaUpgradeModuleVO.setDetails(info.getDesc());
                otaUpgradeModuleVO.setCreated(DateUtils.dealDateFormat(info.getGmtCreate()));
                otaUpgradeModuleVOListt.add(otaUpgradeModuleVO);
            }
            total=response.getBody().getData().size();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, otaUpgradeModuleVOListt);
    }

    @Override
    public void create(OTAUpgradeModuleCreateRq createRq, String operator) throws BusinessException {
        try {
            otaUpgradeManager.createOTAModule(createRq);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void update(OTAUpgradeModuleUpdateRq updateRq, String operator) throws BusinessException {
        try {
            otaUpgradeManager.updateOTAModule(updateRq);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void delete(OTAUpgradeModuleDeleteRq deleteRq, String operator) throws BusinessException {
        try {
            otaUpgradeManager.deleteOTAModule(deleteRq);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}


