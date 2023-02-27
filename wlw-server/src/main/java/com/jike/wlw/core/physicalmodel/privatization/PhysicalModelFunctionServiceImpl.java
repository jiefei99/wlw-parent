package com.jike.wlw.core.physicalmodel.privatization;

import com.alibaba.fastjson.JSON;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelFunction;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelModule;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelModuleDao;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.DirectionType;
import com.jike.wlw.service.physicalmodel.PhysicalModelFunctionFilter;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelDataStandardService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelEvent;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelIOParm;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelProperties;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunction;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionDelRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionModifyRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.DataAndType;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.EnumBoolSpecs;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.EventModel;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.InOutputData;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.Model;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.PropertiesModel;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.ServiceModel;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.Specs;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.SpecsFactory;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.StructSpecs;
import com.jike.wlw.service.physicalmodel.privatization.vo.PhysicalModelFunctionVO;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if (CollectionUtils.isNotEmpty(createRq.getProperties())) {
            for (ModelProperties property : createRq.getProperties()) {
                //添加属性物模型
                savePropertie(tenantId, property, createRq.getModelModuleId(), operator);
            }
        }
        if (CollectionUtils.isNotEmpty(createRq.getServices())) {
            for (ModelService service : createRq.getServices()) {
                //添加服务物模型
                saveService(tenantId, service, createRq.getModelModuleId(), operator);
            }
        }
        if (CollectionUtils.isNotEmpty(createRq.getEvents())) {
            for (ModelEvent event : createRq.getEvents()) {
                //添加事件物模型
                saveEvent(tenantId, event, createRq.getModelModuleId(), operator);
            }
        }
    }

    @Override
    public void modify(String tenantId, PhysicalModelFunctionModifyRq modifyRq, String operator) throws BusinessException {
        if (modifyRq == null) {
            throw new BusinessException("物模型功能参数不能为空");
        }
        if (StringUtil.isNullOrBlank(modifyRq.getModuleIdentifier())) {
            throw new BusinessException("功能所属物模型Id不能为空");
        }
        if (StringUtil.isNullOrBlank(modifyRq.getProductKey())) {
            throw new BusinessException("productKey不能为空");
        }
        if (StringUtil.isNullOrBlank(modifyRq.getIdentifier())) {
            throw new BusinessException("功能标识符不能为空");
        }
        if (StringUtil.isNullOrBlank(tenantId)) {
            throw new BusinessException("租户不能为空");
        }
        try {
            //获取原数据
            get(tenantId,modifyRq.getProductKey(),modifyRq.getModuleIdentifier(),modifyRq.getIdentifier());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void delete(String tenantId, PhysicalModelFunctionDelRq delRq, String operator) throws BusinessException {
        PhysicalModelFunctionFilter filter = new PhysicalModelFunctionFilter();
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, delRq.getProductKey(), StringUtils.isNotBlank(delRq.getModuleIdentifier()) ? delRq.getModuleIdentifier() : "default");
            if (modelModule == null) {
                throw new BusinessException("物模型模块不存在");
            }
            filter.setModelModuleIdEq(modelModule.getUuid());
            filter.setIdentifierIn(delRq.getIdentifierIn());
            filter.setTenantIdEq(tenantId);
            List<PPhysicalModelFunction> query = functionDao.query(filter);
            if (CollectionUtils.isEmpty(query)) {
                return;
            }
            query.parallelStream().forEach(item -> item.setIsDeleted(1));
            functionDao.save(query);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public List<PhysicalModelFunctionVO> query(String tenantId, String productKey, String moduleIdentifier) throws BusinessException {
        List<PhysicalModelFunctionVO> functionVOList = new ArrayList<>();
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, productKey, StringUtils.isNotBlank(moduleIdentifier) ? moduleIdentifier : "default");
            PhysicalModelFunctionFilter filter = new PhysicalModelFunctionFilter();
            filter.setTenantIdEq(tenantId);
            filter.setModelModuleIdEq(modelModule.getUuid());
            List<PPhysicalModelFunction> query = functionDao.query(filter);
            if (CollectionUtils.isNotEmpty(query)) {
                for (PPhysicalModelFunction pPhysicalModelFunction : query) {
                    PhysicalModelFunctionVO vo = new PhysicalModelFunctionVO();
                    BeanUtils.copyProperties(pPhysicalModelFunction, vo);
                    functionVOList.add(vo);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return functionVOList;
    }

    @Override
    public Model get(String tenantId, String productKey, String moduleIdentifier, String identifier) throws BusinessException {
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, productKey, moduleIdentifier);
            PPhysicalModelFunction pFunction = functionDao.get(PPhysicalModelFunction.class, "modelModuleId", modelModule.getUuid(), "identifier", identifier,"isDeleted",0);
            if (ThingModelJsonType.properties.equals(pFunction.getType())){
                PropertiesModel properties = buildModelProperties(pFunction);
                if (DataType.STRUCT.equals(pFunction.getDataType())||(DataType.ARRAY.equals(pFunction.getDataType())&&DataType.STRUCT.equals(pFunction.getArrayType()))){
                    List<PhysicalModelFunction> functionList=queryStructFunction(tenantId,Arrays.asList(pFunction.getUuid()));
                    List<String> dataStandardIds = functionList.parallelStream().map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                    List<PhysicalModelDataStandard> dataStandards = dataStandardService.query(tenantId, dataStandardIds);
                    Map<String, PhysicalModelDataStandard> physicalModelDataStandardMap=dataStandards.parallelStream().collect(Collectors.toMap(PhysicalModelDataStandard::getParentId,PhysicalModelDataStandard->PhysicalModelDataStandard));
                    if (DataType.STRUCT.equals(pFunction.getDataType())){
                        DataAndType dataAndType = new DataAndType();
                        dataAndType.setType(DataType.STRUCT);
                        List<StructSpecs> specsList=new ArrayList<>();
                        for (PhysicalModelFunction physicalModelFunction : functionList) {
                            StructSpecs specs=new StructSpecs();
                            specs.setIdentifier(physicalModelFunction.getIdentifier());
                            specs.setName(physicalModelFunction.getName());
                            DataAndType childDataAndType=new DataAndType();
                            if (physicalModelDataStandardMap.get(physicalModelFunction.getUuid())!=null){
                                childDataAndType.setType(physicalModelDataStandardMap.get(physicalModelFunction.getUuid()).getType());
                                Specs convertSpecs = SpecsFactory.getInvokeSpecs(physicalModelDataStandardMap.get(physicalModelFunction.getUuid()).getType()).convert(physicalModelFunction.getUuid(), physicalModelDataStandardMap);
                                childDataAndType.setSpecs(convertSpecs);
                            }
                            specs.setDataType(childDataAndType);
                            specsList.add(specs);
                        }
                        dataAndType.setSpecs(specsList);
                        properties.setDataType(dataAndType);
                    }
                } else if (DataType.ARRAY.equals(pFunction.getDataType())){
                    properties.setArraySize(pFunction.getArraySize());
                    properties.setArrayType(pFunction.getArrayType());
                } else{
                    PhysicalModelDataStandard standard = dataStandardService.get(tenantId, pFunction.getUuid());
                    DataType type = standard.getType();
                    Specs specs = SpecsFactory.getInvokeSpecs(type);
                    specs.convert(pFunction.getUuid(),new HashMap<String,PhysicalModelDataStandard>(){{put(pFunction.getUuid(),standard);}});
                    DataAndType dataType=new DataAndType();
                    dataType.setType(type);
                    dataType.setSpecs(specs);
                    properties.setDataType(dataType);
                }
                return properties;
            }else if (ThingModelJsonType.events.equals(pFunction.getType())||ThingModelJsonType.services.equals(pFunction.getType())){
                List<PhysicalModelFunction> functionList=queryStructFunction(tenantId,Arrays.asList(pFunction.getUuid()));
                List<String> childStructFunctionIds = functionList.parallelStream().filter(item->DataType.STRUCT.equals(item.getDataType())||DataType.ARRAY.equals(item.getArrayType())).map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                List<String> childNoStructFunctionIds = functionList.parallelStream().filter(item->!DataType.STRUCT.equals(item.getDataType()) && !DataType.ARRAY.equals(item.getDataType())).map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                List<PhysicalModelFunction> childStructFunctionList = queryStructFunction(tenantId, childStructFunctionIds);
                List<String> thirdStructFunctionIds= childStructFunctionList.parallelStream().map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                List<String> dataStandardIds=new ArrayList();
                dataStandardIds.addAll(childNoStructFunctionIds);
                dataStandardIds.addAll(thirdStructFunctionIds);
                List<PhysicalModelDataStandard> DataStandardList = dataStandardService.query(tenantId, dataStandardIds);
                if (ThingModelJsonType.events.equals(pFunction.getType())){
                    EventModel eventModel = buildModelEvent(pFunction);
                    List<InOutputData> outputDatas=new ArrayList<>();
                    eventModel.setOutputData(outputDatas);
                    return eventModel;
                }else {
                    ServiceModel serviceModel=buildModelService(pFunction);
                    List<InOutputData> outputDatas=new ArrayList<>();
                    List<InOutputData> inputDatas=new ArrayList<>();
                    serviceModel.setOutputData(outputDatas);
                    serviceModel.setInputData(inputDatas);
                    return serviceModel;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PropertiesModel buildModelProperties(PPhysicalModelFunction pFunction){
        PropertiesModel properties = new PropertiesModel();
        properties.setType(pFunction.getDataType());
        properties.setDetails(pFunction.getDetails());
        properties.setName(pFunction.getName());
        properties.setIdentifier(pFunction.getIdentifier());
        properties.setRwFlag(pFunction.getRwFlag());
        return properties;
    }
    private EventModel buildModelEvent(PPhysicalModelFunction pFunction){
        EventModel eventModel = new EventModel();
        eventModel.setType(pFunction.getDataType());
        eventModel.setDetails(pFunction.getDetails());
        eventModel.setName(pFunction.getName());
        eventModel.setIdentifier(pFunction.getIdentifier());
        eventModel.setEventType(pFunction.getEventType());
        return eventModel;
    }
    private ServiceModel buildModelService(PPhysicalModelFunction pFunction){
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setType(pFunction.getDataType());
        serviceModel.setDetails(pFunction.getDetails());
        serviceModel.setName(pFunction.getName());
        serviceModel.setIdentifier(pFunction.getIdentifier());
        serviceModel.setCallType(pFunction.getCallType());
        return serviceModel;
    }

    private List<PhysicalModelFunction> queryStructFunction(String tenantId,List<String> parentIds){
        PhysicalModelFunctionFilter filter=new PhysicalModelFunctionFilter();
        filter.setTenantIdEq(tenantId);
        filter.setParentIdIn(parentIds);
        List<PPhysicalModelFunction> query = functionDao.query(filter);
        List<PhysicalModelFunction> functionList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(query)){
            for (PPhysicalModelFunction function : query) {
                PhysicalModelFunction physicalModelFunction = new PhysicalModelFunction();
                BeanUtils.copyProperties(function,physicalModelFunction);
                functionList.add(physicalModelFunction);
            }
        }
        return functionList;
    }

    //属性物模型的添加
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
            DataType type = null;
            Map specsMap = JSON.parseObject(properties.getDataType(), Map.class);
            if (DataType.ARRAY.equals(properties.getType())) {
                pFunction.setArraySize((int) specsMap.get("size"));
                Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                type = DataType.valueOf(itemMap.get("type").toString());
                pFunction.setArrayType(type);
            }
            functionDao.save(pFunction);
            if (DataType.STRUCT.equals(properties.getType())) {
                List specs = (List) specsMap.get("specs");
                for (Object spec : specs) {
                    saveStructSpec(tenantId, pFunction.getUuid(), spec.toString(), operator);
                }
            } else if (DataType.ARRAY.equals(properties.getType())) {
                if (DataType.STRUCT.equals(type)) {
                    Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                    List specs = (List) itemMap.get("specs");
                    for (Object spec : specs) {
                        saveStructSpec(tenantId, pFunction.getUuid(), spec.toString(), operator);
                    }
                }
            } else {
                saveDataStandard(tenantId, properties.getType(), pFunction.getUuid(), specsMap.get("specs").toString(), operator);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    //服务物模型的添加
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
            if (CollectionUtils.isNotEmpty(service.getOutputParams())) {
                saveIOParm(tenantId, pFunction.getUuid(), DirectionType.PARAM_OUTPUT, operator, service.getOutputParams());
            }
            if (CollectionUtils.isNotEmpty(service.getInputParams())) {
                saveIOParm(tenantId, pFunction.getUuid(), DirectionType.PARAM_INPUT, operator, service.getInputParams());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    //事件物模型的添加
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
            if (CollectionUtils.isNotEmpty(event.getOutputParams())) {
                saveIOParm(tenantId, pFunction.getUuid(), DirectionType.PARAM_OUTPUT, operator, event.getOutputParams());
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private PPhysicalModelModule doGetModule(String tenantId, String productKey, String identifier) throws Exception {
        PPhysicalModelModule perz = modelModuleDao.get(PPhysicalModelModule.class, "productKey", productKey, "identifier", identifier, "tenantId", tenantId, "isDeleted", 0);
        return perz;
    }

    //设置输入输出参数
    private void saveIOParm(String tenantId, String parentId, DirectionType ioType, String operator, List<ModelIOParm> params) {
        if (CollectionUtils.isNotEmpty(params)) {
            for (ModelIOParm param : params) {
                PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
                pFunction.setTenantId(tenantId);
                pFunction.setName(param.getName());
                pFunction.setIdentifier(param.getIdentifier());
                pFunction.setParentId(parentId);
                pFunction.setDirection(ioType);
                pFunction.onCreated(operator);
                Map map = JSON.parseObject(param.getDataType(), Map.class);
                DataType type = DataType.valueOf(map.get("type").toString());
                pFunction.setDataType(type);
                Map specsMap = null;
                if (DataType.ARRAY.equals(type)) {
                    specsMap = JSON.parseObject(map.get("specs").toString(), Map.class);
                    pFunction.setArraySize((int) specsMap.get("size"));
                }
                try {
                    functionDao.save(pFunction);
                    if (DataType.valueOf((String) map.get("type")) == DataType.STRUCT) {
                        if (map.get("specs") != null) {
                            List specs = (List) map.get("specs");
                            for (Object spec : specs) {
                                saveStructSpec(tenantId, pFunction.getUuid(), spec.toString(), operator);
                            }
                        }
                    } else if (DataType.ARRAY.equals(type)) {
                        Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                        if (itemMap.get("specs") != null) {
                            List itemSpecs = (List) itemMap.get("specs");
                            for (Object itemSpec : itemSpecs) {
                                saveStructSpec(tenantId, pFunction.getUuid(), itemSpec.toString(), operator);
                            }
                        }
                    } else {
                        saveDataStandard(tenantId, DataType.valueOf((String) map.get("type")), pFunction.getUuid(), map.get("specs").toString(), operator);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //设置对象类型参数
    private void saveStructSpec(String tenantId, String parentId, String spec, String operator) {
        try {
            Map mapSpec = JSON.parseObject(spec, Map.class);
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setName(mapSpec.get("name").toString());
            pFunction.setIdentifier(mapSpec.get("identifier").toString());
            pFunction.setParentId(parentId);
            pFunction.onCreated(operator);
            Map map = JSON.parseObject(mapSpec.get("dataType").toString(), Map.class);
            pFunction.setDataType(DataType.valueOf((String) map.get("type")));
            functionDao.save(pFunction);
            saveDataStandard(tenantId, DataType.valueOf((String) map.get("type")), pFunction.getUuid(), map.get("specs").toString(), operator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加标准
    private void saveDataStandard(String tenantId, DataType type, String parentId, String specs, String operator) {
        PhysicalModelDataStandardCreateRq dataStandardCreateRq = new PhysicalModelDataStandardCreateRq();
        dataStandardCreateRq.setParentId(parentId);
        dataStandardCreateRq.setTenantId(tenantId);
        dataStandardCreateRq.setDataType(type);
        dataStandardCreateRq.setDataSpecs(specs);
        dataStandardService.create(tenantId, dataStandardCreateRq, operator);
    }
}


