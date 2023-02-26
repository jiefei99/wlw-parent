package com.jike.wlw.core.physicalmodel.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelFunction;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelModule;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelModuleDao;
import com.jike.wlw.service.physicalmodel.PhysicalModelFunctionFilter;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelDataStandardService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelEvent;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelProperties;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionDelRq;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: PhysicalModelFunctionServiceImpl
 * @Author RS
 * @Date: 2023/2/17 14:22
 * @Version 1.0
 */

@Slf4j
@RestController("modelFunctionServicePrivateImpl")
@ApiModel("私有化物模型功能服务实现")
public class PhysicalModelFunctionServiceImpl implements PhysicalModelFunctionService {
    @Autowired
    private PhysicalModelFunctionDao functionDao;
    @Autowired
    private PhysicalModelDataStandardService dataStandardService;
    @Autowired
    private PhysicalModelModuleDao modelModuleDao;

    @Override
    public void create(String tenantId, PhysicalModelFunctionCreateRq createRq, String operator) throws BusinessException {
        if (createRq == null) {
            throw new BusinessException("物模型功能参数不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getModelModuleId())) {
            throw new BusinessException("功能所属物模型Id不能为空");
        }
        if (CollectionUtils.isEmpty(createRq.getServices()) || CollectionUtils.isEmpty(createRq.getEvents()) || CollectionUtils.isEmpty(createRq.getProperties())) {
            throw new BusinessException("物模型功能不能为空");
        }
        if (StringUtil.isNullOrBlank(tenantId)) {
            throw new BusinessException("租户不能为空");
        }
        //属性不为空
        if (CollectionUtils.isNotEmpty(createRq.getProperties())) {
            for (ModelProperties property : createRq.getProperties()) {
                savePropertie(tenantId, property, createRq.getModelModuleId(), operator);
            }
        }
        if (CollectionUtils.isNotEmpty(createRq.getServices())) {
            for (ModelService service : createRq.getServices()) {
                saveService(tenantId, service, createRq.getModelModuleId(), operator);
            }
        }
        if (CollectionUtils.isNotEmpty(createRq.getEvents())) {
            for (ModelEvent event : createRq.getEvents()) {
                saveEvent(tenantId, event, createRq.getModelModuleId(), operator);
            }
        }
    }

    @Override
    public void delete(String tenantId, PhysicalModelFunctionDelRq delRq, String operator) throws BusinessException {
        PhysicalModelFunctionFilter filter =new PhysicalModelFunctionFilter();
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, delRq.getProductKey(), StringUtils.isNotBlank(delRq.getModuleIdentifier()) ? delRq.getModuleIdentifier() : "default");
            if (modelModule==null){
                throw new BusinessException("物模型模块不存在");
            }
            filter.setModelModuleIdEq(modelModule.getUuid());
            filter.setIdentifierIn(delRq.getIdentifierIn());
            filter.setTenantIdEq(tenantId);
            List<PPhysicalModelFunction> query = functionDao.query(filter);
            if (CollectionUtils.isEmpty(query)){
                return;
            }
            query.parallelStream().forEach(item->item.setIsDeleted(1));
            functionDao.save(query);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void savePropertie(String tenantId, ModelProperties properties, String modelModuleId, String operator) {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setDataType(properties.getDataType());
            pFunction.setName(properties.getName());
            pFunction.setModelModuleId(modelModuleId);
            pFunction.setIdentifier(properties.getIdentifier());
            pFunction.setType(ThingModelJsonType.properties);
            pFunction.setRwFlag(properties.getRwFlag());
            pFunction.setDetails(properties.getDetails());
            pFunction.setRequired(false);
            pFunction.onCreated(operator);
            functionDao.save(pFunction);
            if (StringUtils.isNotBlank(properties.getDataSpecs())||CollectionUtils.isNotEmpty(properties.getDataSpecsList())){
                PhysicalModelDataStandardCreateRq dataStandardCreateRq=new PhysicalModelDataStandardCreateRq();
                dataStandardCreateRq.setParentId(pFunction.getUuid());
                dataStandardCreateRq.setDataType(properties.getDataType());
                dataStandardCreateRq.setDataSpecs(properties.getDataSpecs());
                dataStandardCreateRq.setDataSpecsList(properties.getDataSpecsList());
                dataStandardService.create(tenantId,dataStandardCreateRq,operator);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void saveService(String tenantId, ModelService service, String modelModuleId, String operator) {
        try {
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setName(service.getName());
            pFunction.setIdentifier(service.getIdentifier());
            pFunction.setModelModuleId(modelModuleId);
            pFunction.setType(ThingModelJsonType.services);
            pFunction.setDetails(service.getDetails());
            pFunction.setRequired(true);
            pFunction.setCallType(service.getCallType());
            pFunction.onCreated(operator);
            functionDao.save(pFunction);
            if (service.getInputParams()!=null||service.getOutputParams()!=null){
                PhysicalModelDataStandardCreateRq dataStandardCreateRq=new PhysicalModelDataStandardCreateRq();
                dataStandardCreateRq.setParentId(pFunction.getUuid());
                dataStandardCreateRq.setInputParams(service.getInputParams());
                dataStandardCreateRq.setOutputParams(service.getOutputParams());
                dataStandardService.create(tenantId,dataStandardCreateRq,operator);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void saveEvent(String tenantId, ModelEvent event, String modelModuleId, String operator) {
        try {
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setName(event.getName());
            pFunction.setIdentifier(event.getIdentifier());
            pFunction.setModelModuleId(modelModuleId);
            pFunction.setType(ThingModelJsonType.events);
            pFunction.setDetails(event.getDetails());
            pFunction.setRequired(true);
            pFunction.onCreated(operator);
            pFunction.setEventType(event.getEventType());
            functionDao.save(pFunction);
            if (event.getOutputParams()!=null){
                PhysicalModelDataStandardCreateRq dataStandardCreateRq=new PhysicalModelDataStandardCreateRq();
                dataStandardCreateRq.setParentId(pFunction.getUuid());
                dataStandardCreateRq.setOutputParams(event.getOutputParams());
                dataStandardService.create(tenantId,dataStandardCreateRq,operator);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private PPhysicalModelModule doGetModule (String tenantId,String productKey,String identifier) throws Exception {
        PPhysicalModelModule perz = modelModuleDao.get(PPhysicalModelModule.class, "productKey", productKey,"identifier",identifier,"tenantId",tenantId,"isDeleted",0);
        return perz;
    }
}


