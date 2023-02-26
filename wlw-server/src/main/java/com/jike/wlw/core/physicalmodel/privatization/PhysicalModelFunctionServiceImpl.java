package com.jike.wlw.core.physicalmodel.privatization;

import com.alibaba.fastjson.JSON;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelFunction;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelModule;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelModuleDao;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.PhysicalModelFunctionFilter;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelDataStandardService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.*;
import com.jike.wlw.service.physicalmodel.privatization.vo.PhysicalModelFunctionVO;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

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
    public void modify(String tenantId, PhysicalModelFunctionModifyRq modifyRq, String operator) throws BusinessException {
        if (modifyRq == null) {
            throw new BusinessException("物模型功能参数不能为空");
        }
        if (StringUtil.isNullOrBlank(modifyRq.getModelModuleId())) {
            throw new BusinessException("功能所属物模型Id不能为空");
        }
        if (CollectionUtils.isEmpty(modifyRq.getServices()) || CollectionUtils.isEmpty(modifyRq.getEvents()) || CollectionUtils.isEmpty(modifyRq.getProperties())) {
            throw new BusinessException("物模型功能不能为空");
        }
        if (StringUtil.isNullOrBlank(tenantId)) {
            throw new BusinessException("租户不能为空");
        }
        //属性不为空
        if (CollectionUtils.isNotEmpty(modifyRq.getProperties())) {

        }
        if (CollectionUtils.isNotEmpty(modifyRq.getServices())) {
        }
        if (CollectionUtils.isNotEmpty(modifyRq.getEvents())) {
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

    @Override
    public void get(String tenantId, String moduleId, String functionId) throws BusinessException {
        try {
            functionDao.get(PPhysicalModelFunction.class,"tenantId",tenantId,"modelModuleId",moduleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PhysicalModelFunctionVO> query(String tenantId, String moduleId) throws BusinessException {
        PhysicalModelFunctionFilter filter=new PhysicalModelFunctionFilter();
        filter.setTenantIdEq(tenantId);
        filter.setModelModuleIdEq(moduleId);
        List<PPhysicalModelFunction> query = functionDao.query(filter);
        List<PhysicalModelFunctionVO> functionVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(query)){
            for (PPhysicalModelFunction pPhysicalModelFunction : query) {
                PhysicalModelFunctionVO vo=new PhysicalModelFunctionVO();
                BeanUtils.copyProperties(pPhysicalModelFunction,vo);
                functionVOList.add(vo);
            }
        }
        return functionVOList;
    }

    private void savePropertie(String tenantId, ModelProperties properties, String modelModuleId, String operator) {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setDataType(properties.getType());
            pFunction.setName(properties.getName());
            pFunction.setModelModuleId(modelModuleId);
            pFunction.setIdentifier(properties.getIdentifier());
            pFunction.setType(ThingModelJsonType.properties);
            pFunction.setRwFlag(properties.getRwFlag());
            pFunction.setDetails(properties.getDetails());
            pFunction.setRequired(false);
            pFunction.onCreated(operator);
            DataType type=null;
            Map specsMap = JSON.parseObject(properties.getDataType(), Map.class);
            if (DataType.ARRAY.equals(properties.getType())){
                pFunction.setArraySize((int)specsMap.get("size"));
                Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                type = DataType.valueOf(itemMap.get("type").toString());
                pFunction.setArrayType(type);
            }
            functionDao.save(pFunction);
            if (DataType.STRUCT.equals(properties.getType())){
                List specs = (List) specsMap.get("specs");
                for (Object spec : specs) {
                    saveStructSpec(tenantId,pFunction.getUuid(),spec.toString(),operator);
                }
            }else if(DataType.ARRAY.equals(properties.getType())){
                if (DataType.STRUCT.equals(type)){
                    Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                    List specs=(List)itemMap.get("specs");
                    for (Object spec : specs) {
                        saveStructSpec(tenantId,pFunction.getUuid(),spec.toString(),operator);
                    }
                }
            }else{
                saveDataStandard(tenantId,properties.getType(),pFunction.getUuid(),specsMap.get("specs").toString(),operator);
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
            if (CollectionUtils.isNotEmpty(service.getOutputParams())){
                saveIOParm(tenantId,pFunction.getUuid(),"OUTPUT",operator,service.getOutputParams());
            }
            if (CollectionUtils.isNotEmpty(service.getInputParams())){
                saveIOParm(tenantId,pFunction.getUuid(),"INPUT",operator,service.getInputParams());
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
            if (CollectionUtils.isNotEmpty(event.getOutputParams())){
                saveIOParm(tenantId,pFunction.getUuid(),"Output",operator,event.getOutputParams());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private PPhysicalModelModule doGetModule (String tenantId,String productKey,String identifier) throws Exception {
        PPhysicalModelModule perz = modelModuleDao.get(PPhysicalModelModule.class, "productKey", productKey,"identifier",identifier,"tenantId",tenantId,"isDeleted",0);
        return perz;
    }

    private void saveIOParm(String tenantId,String parentId,String ioType,String operator,List<ModelIOParm> params){
        if (CollectionUtils.isNotEmpty(params)){
            for (ModelIOParm param : params) {
                PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
                pFunction.setTenantId(tenantId);
                pFunction.setName(param.getName());
                pFunction.setIdentifier(param.getIdentifier());
                pFunction.setParentId(parentId);
                pFunction.setIoParmType(ioType);
                pFunction.onCreated(operator);
                Map map = JSON.parseObject(param.getDataType(), Map.class);
                DataType type = DataType.valueOf(map.get("type").toString());
                pFunction.setDataType(type);
                Map specsMap=null;
                if (DataType.ARRAY.equals(type)){
                    specsMap = JSON.parseObject(map.get("specs").toString(), Map.class);
                    pFunction.setArraySize((int)specsMap.get("size"));
                }
                try {
                    functionDao.save(pFunction);
                    if (DataType.valueOf((String)map.get("type"))==DataType.STRUCT){
                        if (map.get("specs")!=null){
                            List specs = (List)map.get("specs");
                            for (Object spec : specs) {
                                saveStructSpec(tenantId,pFunction.getUuid(),spec.toString(),operator);
                            }
                        }
                    }else if (DataType.ARRAY.equals(type)){
                        Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                        if (itemMap.get("specs")!=null){
                            List itemSpecs = (List) itemMap.get("specs");
                            for (Object itemSpec : itemSpecs) {
                                saveStructSpec(tenantId,pFunction.getUuid(),itemSpec.toString(),operator);
                            }
                        }
                    }else{
                        saveDataStandard(tenantId,DataType.valueOf((String)map.get("type")),pFunction.getUuid(),map.get("specs").toString(),operator);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void saveStructSpec(String tenantId,String parentId,String spec,String operator){
        try {
            Map mapSpec = JSON.parseObject(spec, Map.class);
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setName(mapSpec.get("name").toString());
            pFunction.setIdentifier(mapSpec.get("identifier").toString());
            pFunction.setParentId(parentId);
            pFunction.onCreated(operator);
            Map map = JSON.parseObject(mapSpec.get("dataType").toString(), Map.class);
            pFunction.setDataType(DataType.valueOf((String)map.get("type")));
            functionDao.save(pFunction);
            saveDataStandard(tenantId,DataType.valueOf((String)map.get("type")),pFunction.getUuid(),map.get("specs").toString(),operator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDataStandard(String tenantId,DataType type,String parentId,String specs,String operator){
        PhysicalModelDataStandardCreateRq dataStandardCreateRq=new PhysicalModelDataStandardCreateRq();
        dataStandardCreateRq.setParentId(parentId);
        dataStandardCreateRq.setTenantId(tenantId);
        dataStandardCreateRq.setDataType(type);
        dataStandardCreateRq.setDataSpecs(specs);
        dataStandardService.create(tenantId,dataStandardCreateRq,operator);
    }
}


