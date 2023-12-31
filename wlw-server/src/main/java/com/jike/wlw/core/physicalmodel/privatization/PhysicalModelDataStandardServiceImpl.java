package com.jike.wlw.core.physicalmodel.privatization;

import com.alibaba.fastjson.JSON;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelDataStandard;
import com.jike.wlw.dao.physicalmodel.PhysicalModelDataStandardDao;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.PhysicalModelDataStandardFilter;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelDataStandardService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.DateTextDataStandards;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.NumberDataStandards;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: PhysicalModelDataStandardServiceImpl
 * @Author RS
 * @Date: 2023/2/17 19:46
 * @Version 1.0
 */

@Slf4j
@RestController("modelDataStandardServicePrivateImpl")
@ApiModel("私有化物模型数据标准实现")
public class PhysicalModelDataStandardServiceImpl implements PhysicalModelDataStandardService {
    @Autowired
    private PhysicalModelDataStandardDao dataStandardDao;

    @Override
    public void create(String tenantId, PhysicalModelDataStandardCreateRq createRq, String operator) throws BusinessException {
        if (createRq == null) {
            throw new BusinessException("物模型功能参数不能为空");
        }
        if (StringUtil.isNullOrBlank(tenantId)) {
            throw new BusinessException("租户不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getParentId())) {
            throw new BusinessException("父类Id不能为空");
        }
        try {
            if (DataType.INT.equals(createRq.getDataType()) || DataType.FLOAT.equals(createRq.getDataType()) || DataType.DOUBLE.equals(createRq.getDataType())) {
                saveNumberDataStandard(tenantId, createRq.getDataType(),createRq.getParentId(), createRq.getDataSpecs(), operator);
            } else if (DataType.TEXT.equals(createRq.getDataType()) || DataType.DATE.equals(createRq.getDataType())) {
                saveDateTextDataStandard(tenantId, createRq.getDataType(),createRq.getParentId(), createRq.getDataSpecs(), operator);
            }
            if (DataType.ENUM.equals(createRq.getDataType()) || DataType.BOOL.equals(createRq.getDataType())) {
                saveEnumBoolDataStandard(tenantId, createRq.getDataType(),createRq.getParentId(), createRq.getDataSpecs(), operator);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());

        }
    }

    @Override
    public PhysicalModelDataStandard get(String tenantId, String parentId) throws BusinessException {
        if (StringUtil.isNullOrBlank(tenantId)) {
            throw new BusinessException("租户不能为空");
        }
        if (StringUtil.isNullOrBlank(parentId)) {
            throw new BusinessException("父类Id不能为空");
        }
        try {
            PPhysicalModelDataStandard pPhysicalModelDataStandard = dataStandardDao.get(PPhysicalModelDataStandard.class, "tenantId", tenantId, "parentId", parentId,"isDeleted",0);
            PhysicalModelDataStandard physicalModelDataStandards = new PhysicalModelDataStandard();
            BeanUtils.copyProperties(pPhysicalModelDataStandard,physicalModelDataStandards);
            return physicalModelDataStandards;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public List<PhysicalModelDataStandard> query(String tenantId, List<String> parentIds) throws BusinessException {
        if (StringUtil.isNullOrBlank(tenantId)) {
            throw new BusinessException("租户不能为空");
        }
        if (CollectionUtils.isEmpty(parentIds)) {
            throw new BusinessException("父类Id集合不能为空");
        }
        try {
            PhysicalModelDataStandardFilter filter =new PhysicalModelDataStandardFilter();
            filter.setTenantIdEq(tenantId);
            filter.setParentIdIn(parentIds);
            List<PPhysicalModelDataStandard> query = dataStandardDao.query(filter);
            List<PhysicalModelDataStandard> dataStandardList=new ArrayList<>();
            if (CollectionUtils.isNotEmpty(query)){
                for (PPhysicalModelDataStandard pPhysicalModelDataStandard : query) {
                    PhysicalModelDataStandard dataStandard=new PhysicalModelDataStandard();
                    BeanUtils.copyProperties(pPhysicalModelDataStandard,dataStandard);
                    dataStandardList.add(dataStandard);
                }
            }
            return dataStandardList;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void saveNumberDataStandard(String tenantId, DataType dataType,String parentId, String dataSpecs, String operator) {
        PPhysicalModelDataStandard pPhysicalModelDataStandard = new PPhysicalModelDataStandard();
        try {
            NumberDataStandards numberDataStandards = JSON.parseObject(dataSpecs, NumberDataStandards.class);
            pPhysicalModelDataStandard.setTenantId(tenantId);
            pPhysicalModelDataStandard.onCreated(operator);
            pPhysicalModelDataStandard.setType(dataType.name());
            if (DataType.INT.equals(dataType)) {
                pPhysicalModelDataStandard.setMax(StringUtils.isNotBlank(numberDataStandards.getMax()) ? numberDataStandards.getMax() : "2147483647");
                pPhysicalModelDataStandard.setMin(StringUtils.isNotBlank(numberDataStandards.getMin()) ? numberDataStandards.getMin() : "-2147483648");
                pPhysicalModelDataStandard.setStep(StringUtils.isNotBlank(numberDataStandards.getStep()) ? numberDataStandards.getStep() : "1");
            } else if (DataType.FLOAT.equals(dataType)) {
                pPhysicalModelDataStandard.setMax(StringUtils.isNotBlank(numberDataStandards.getMax()) ? numberDataStandards.getMax() : "3.4028235E38");
                pPhysicalModelDataStandard.setMin(StringUtils.isNotBlank(numberDataStandards.getMin()) ? numberDataStandards.getMin() : "-1.4E-45");
                pPhysicalModelDataStandard.setStep(StringUtils.isNotBlank(numberDataStandards.getStep()) ? numberDataStandards.getStep() : "0.1");
            } else if (DataType.DOUBLE.equals(dataType)) {
                pPhysicalModelDataStandard.setMax(StringUtils.isNotBlank(numberDataStandards.getMax()) ? numberDataStandards.getMax() : "1.7976931348623157E308");
                pPhysicalModelDataStandard.setMin(StringUtils.isNotBlank(numberDataStandards.getMin()) ? numberDataStandards.getMin() : "-4.9E-324");
                pPhysicalModelDataStandard.setStep(StringUtils.isNotBlank(numberDataStandards.getStep()) ? numberDataStandards.getStep() : "0.01");
            }
            pPhysicalModelDataStandard.setUnit(numberDataStandards.getUnit());
            pPhysicalModelDataStandard.setUnitName(numberDataStandards.getUnitName());
            pPhysicalModelDataStandard.setParentId(parentId);
            dataStandardDao.save(pPhysicalModelDataStandard);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void saveDateTextDataStandard(String tenantId, DataType dataType, String parentId, String dataSpecs, String operator) {
        PPhysicalModelDataStandard pPhysicalModelDataStandard = new PPhysicalModelDataStandard();
        try {
            DateTextDataStandards dateTextDataStandards = JSON.parseObject(dataSpecs, DateTextDataStandards.class);
            pPhysicalModelDataStandard.setTenantId(tenantId);
            pPhysicalModelDataStandard.onCreated(operator);
            pPhysicalModelDataStandard.setParentId(parentId);
            pPhysicalModelDataStandard.setType(dataType.name());
            if (DataType.TEXT.equals(dataType)) {
                pPhysicalModelDataStandard.setLength(dateTextDataStandards.getLength());
            }
            dataStandardDao.save(pPhysicalModelDataStandard);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void saveEnumBoolDataStandard(String tenantId, DataType dataType, String parentId, String dataSpecs, String operator) {
        PPhysicalModelDataStandard pPhysicalModelDataStandard = new PPhysicalModelDataStandard();
        try {
            pPhysicalModelDataStandard.setTenantId(tenantId);
            pPhysicalModelDataStandard.onCreated(operator);
            pPhysicalModelDataStandard.setParentId(parentId);
            pPhysicalModelDataStandard.setBoolEnumRemark(dataSpecs);
            pPhysicalModelDataStandard.setType(dataType.name());
            dataStandardDao.save(pPhysicalModelDataStandard);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}


