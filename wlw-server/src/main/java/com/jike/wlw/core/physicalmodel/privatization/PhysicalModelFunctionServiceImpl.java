package com.jike.wlw.core.physicalmodel.privatization;

import com.alibaba.fastjson.JSON;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelFunction;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelModule;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelModuleDao;
import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.DirectionType;
import com.jike.wlw.service.physicalmodel.EventType;
import com.jike.wlw.service.physicalmodel.PhysicalModelFunctionFilter;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelDataStandardService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelEvent;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelIOParm;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelProperties;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.PhysicalModelFunction;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.PhysicalModelFunctionCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.PhysicalModelFunctionDelRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.PhysicalModelFunctionModifyRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.*;
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
        if (CollectionUtils.isEmpty(createRq.getServices()) && CollectionUtils.isEmpty(createRq.getEvents()) && CollectionUtils.isEmpty(createRq.getProperties())) {
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
            PhysicalModelFunctionDelRq delRq = new PhysicalModelFunctionDelRq();
            delRq.setProductKey(modifyRq.getProductKey());
            delRq.setModuleIdentifier(modifyRq.getModuleIdentifier());
            delRq.setIdentifierEq(modifyRq.getIdentifier());
            delete(tenantId, delRq, operator);
            PhysicalModelFunctionCreateRq createRq =new PhysicalModelFunctionCreateRq();
            createRq.setProductKey(modifyRq.getProductKey());
            createRq.setModelModuleId(modifyRq.getModelModuleId());
            createRq.setEvents(modifyRq.getEvents());
            createRq.setServices(modifyRq.getServices());
            createRq.setProperties(modifyRq.getProperties());
            create(tenantId,createRq,operator);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void delete(String tenantId, PhysicalModelFunctionDelRq delRq, String operator) throws BusinessException {
        PhysicalModelFunctionFilter filter = new PhysicalModelFunctionFilter();
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, delRq.getProductKey(), delRq.getModuleIdentifier());
            if (modelModule == null) {
                throw new BusinessException("物模型模块不存在");
            }
            filter.setModelModuleIdEq(modelModule.getUuid());
            filter.setIdentifierIn(delRq.getIdentifierIn());
            filter.setTenantIdEq(tenantId);
            List<PPhysicalModelFunction> delModelFunctionList = new ArrayList<>();
            //查询具体模型集合
            List<PPhysicalModelFunction> query = functionDao.query(filter);
            if (CollectionUtils.isEmpty(query)) {
                return;
            }
            delModelFunctionList.addAll(query);
            List<String> functionIds = query.parallelStream().map(PPhysicalModelFunction::getUuid).collect(Collectors.toList());
            //数据类型为 struct
            List<PPhysicalModelFunction> structFunction = queryChildFunction(tenantId, functionIds);
            if (CollectionUtils.isNotEmpty(structFunction)) {
                delModelFunctionList.addAll(structFunction);
                List<String> structFunctionIds = structFunction.parallelStream().map(PPhysicalModelFunction::getUuid).collect(Collectors.toList());
                //功能为服务 or 事件 出入参有几率为 struct
                List<PPhysicalModelFunction> childFunction = queryChildFunction(tenantId, structFunctionIds);
                if (CollectionUtils.isNotEmpty(childFunction)) {
                    delModelFunctionList.addAll(childFunction);
                }
            }
            delModelFunctionList.parallelStream().forEach(item -> item.setIsDeleted(1));
            functionDao.save(delModelFunctionList);
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
                    if (StringUtils.isNotBlank(pPhysicalModelFunction.getCallType())){
                        vo.setCallType(CallType.valueOf(pPhysicalModelFunction.getCallType()));
                    }
                    vo.setIdentifier(pPhysicalModelFunction.getIdentifier());
                    vo.setModelModuleId(pPhysicalModelFunction.getModelModuleId());
                    if (StringUtils.isNotBlank(pPhysicalModelFunction.getDataType())){
                        vo.setDataType(DataType.valueOf(pPhysicalModelFunction.getDataType()));
                    }
                    if (StringUtils.isNotBlank(pPhysicalModelFunction.getEventType())){
                        vo.setEventType(EventType.valueOf(pPhysicalModelFunction.getEventType()));
                    }
                    vo.setName(pPhysicalModelFunction.getName());
                    vo.setIdentifier(pPhysicalModelFunction.getIdentifier());
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
        Model model = new Model();
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, productKey, moduleIdentifier);
            if (modelModule == null) {
                return model;
            }
            PPhysicalModelFunction pFunction = functionDao.get(PPhysicalModelFunction.class, "modelModuleId", modelModule.getUuid(), "identifier", identifier, "isDeleted", 0);
            if (pFunction == null) {
                return model;
            }
            if (ThingModelJsonType.properties.name().equals(pFunction.getType())) {
                PropertiesModel properties = buildModelProperties(pFunction);
                if (DataType.STRUCT.name().equals(pFunction.getDataType()) || (DataType.ARRAY.name().equals(pFunction.getDataType()) && DataType.STRUCT.name().equals(pFunction.getArrayType()))) {
                    List<PhysicalModelFunction> functionList = queryStructFunction(tenantId, Arrays.asList(pFunction.getUuid()));
                    List<String> dataStandardIds = functionList.parallelStream().map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                    List<PhysicalModelDataStandard> dataStandards = dataStandardService.query(tenantId, dataStandardIds);
                    Map<String, PhysicalModelDataStandard> physicalModelDataStandardMap = dataStandards.parallelStream().collect(Collectors.toMap(PhysicalModelDataStandard::getParentId, PhysicalModelDataStandard -> PhysicalModelDataStandard));
                    if (DataType.STRUCT.name().equals(pFunction.getDataType())) {
                        DataAndType dataAndType = new DataAndType();
                        dataAndType.setType(DataType.STRUCT);
                        List<StructSpecs> specsList = new ArrayList<>();
                        for (PhysicalModelFunction physicalModelFunction : functionList) {
                            StructSpecs specs = new StructSpecs();
                            specs.setIdentifier(physicalModelFunction.getIdentifier());
                            specs.setName(physicalModelFunction.getName());
                            DataAndType childDataAndType = new DataAndType();
                            if (physicalModelDataStandardMap.get(physicalModelFunction.getUuid()) != null) {
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
                } else if (DataType.ARRAY.name().equals(pFunction.getDataType())) {
                    properties.setArraySize(pFunction.getArraySize());
                    properties.setArrayType(DataType.valueOf(pFunction.getArrayType()));
                } else {
                    PhysicalModelDataStandard standard = dataStandardService.get(tenantId, pFunction.getUuid());
                    DataType type = standard.getType();
                    Specs specs = SpecsFactory.getInvokeSpecs(type);
                    specs.convert(pFunction.getUuid(), new HashMap<String, PhysicalModelDataStandard>() {{
                        put(pFunction.getUuid(), standard);
                    }});
                    DataAndType dataType = new DataAndType();
                    dataType.setType(type);
                    dataType.setSpecs(specs);
                    properties.setDataType(dataType);
                }
                return properties;
            } else {
                List<PhysicalModelFunction> functionList = queryStructFunction(tenantId, Arrays.asList(pFunction.getUuid()));
                if (CollectionUtils.isEmpty(functionList)) {
                    if (ThingModelJsonType.events.name().equals(pFunction.getType())) {
                        EventModel eventModel = buildModelEvent(pFunction);
                        return eventModel;
                    } else if (ThingModelJsonType.services.name().equals(pFunction.getType())) {
                        ServiceModel serviceModel = buildModelService(pFunction);
                        return serviceModel;
                    }
                }
                List<String> childStructFunctionIds = functionList.parallelStream().filter(item -> DataType.STRUCT.equals(item.getDataType()) || DataType.ARRAY.equals(item.getArrayType())).map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                List<String> childNoStructFunctionIds = functionList.parallelStream().filter(item -> !DataType.STRUCT.equals(item.getDataType()) && !DataType.ARRAY.equals(item.getDataType())).map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                List<PhysicalModelFunction> childStructFunctionList = queryStructFunction(tenantId, childStructFunctionIds);
                List<String> thirdStructFunctionIds = childStructFunctionList.parallelStream().map(PhysicalModelFunction::getUuid).collect(Collectors.toList());
                List<String> dataStandardIds = new ArrayList();
                dataStandardIds.addAll(childNoStructFunctionIds);
                dataStandardIds.addAll(thirdStructFunctionIds);
                List<PhysicalModelDataStandard> dataStandardList = dataStandardService.query(tenantId, dataStandardIds);
                if (ThingModelJsonType.events.name().equals(pFunction.getType())) {
                    EventModel eventModel = buildModelEvent(pFunction);
                    addInOutputAttribute(eventModel, ThingModelJsonType.events, functionList, childStructFunctionList, dataStandardList);
                    return eventModel;
                } else {
                    ServiceModel serviceModel = buildModelService(pFunction);
                    addInOutputAttribute(serviceModel, ThingModelJsonType.services, functionList, childStructFunctionList, dataStandardList);
                    return serviceModel;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private void addInOutputAttribute(Model model, ThingModelJsonType type, List<PhysicalModelFunction> functionList, List<PhysicalModelFunction> childStructFunctionList, List<PhysicalModelDataStandard> dataStandardList) {
        List<PhysicalModelFunction> inputFunctionList = new ArrayList<>();
        List<PhysicalModelFunction> inputStructFunctionList = new ArrayList<>();
        List<PhysicalModelFunction> outputFunctionList = new ArrayList<>();
        List<PhysicalModelFunction> outputStructFunctionList = new ArrayList<>();
        for (PhysicalModelFunction physicalModelFunction : functionList) {
            if (DirectionType.PARAM_OUTPUT.equals(physicalModelFunction.getDirection())) {
                if (DataType.STRUCT.equals(physicalModelFunction.getDataType()) || DataType.STRUCT.equals(physicalModelFunction.getArrayType())) {
                    outputStructFunctionList.add(physicalModelFunction);
                } else {
                    outputFunctionList.add(physicalModelFunction);
                }
            } else {
                if (DataType.STRUCT.equals(physicalModelFunction.getDataType()) || DataType.STRUCT.equals(physicalModelFunction.getArrayType())) {
                    inputStructFunctionList.add(physicalModelFunction);
                } else {
                    inputFunctionList.add(physicalModelFunction);
                }
            }
        }
        Map<String, PhysicalModelDataStandard> physicalModelDataStandardMap = dataStandardList.parallelStream().collect(Collectors.toMap(PhysicalModelDataStandard::getParentId, PhysicalModelDataStandard -> PhysicalModelDataStandard));
        List<InOutputData> outputDataList = new ArrayList<>();
        List<InOutputData> inputDataList = new ArrayList<>();
        for (PhysicalModelFunction physicalModelFunction : outputFunctionList) {
            InOutputData outputData = new InOutputData();
            outputData.setIdentifier(physicalModelFunction.getIdentifier());
            outputData.setName(physicalModelFunction.getName());
            DataAndType dataAndType = new DataAndType();
            dataAndType.setType(physicalModelFunction.getDataType());
            if (DataType.ARRAY.equals(physicalModelFunction.getDataType())) {
                ArraySpecs arraySpecs = new ArraySpecs();
                arraySpecs.setSize(physicalModelFunction.getArraySize());
                arraySpecs.setItem(new ItemSpecs(physicalModelFunction.getArrayType()));
                dataAndType.setSpecs(arraySpecs);
            } else {
                Specs specs = SpecsFactory.getInvokeSpecs(physicalModelFunction.getDataType());
                specs.convert(physicalModelFunction.getUuid(), physicalModelDataStandardMap);
                dataAndType.setSpecs(specs);
            }
            outputData.setDataType(dataAndType);
            outputDataList.add(outputData);
        }
        for (PhysicalModelFunction physicalModelFunction : inputFunctionList) {
            InOutputData inputData = new InOutputData();
            inputData.setIdentifier(physicalModelFunction.getIdentifier());
            inputData.setName(physicalModelFunction.getName());
            DataAndType dataAndType = new DataAndType();
            dataAndType.setType(physicalModelFunction.getDataType());
            if (DataType.ARRAY.equals(physicalModelFunction.getDataType())) {
                ArraySpecs arraySpecs = new ArraySpecs();
                arraySpecs.setSize(physicalModelFunction.getArraySize());
                arraySpecs.setItem(new ItemSpecs(physicalModelFunction.getArrayType()));
                dataAndType.setSpecs(arraySpecs);
            } else {
                Specs specs = SpecsFactory.getInvokeSpecs(physicalModelFunction.getDataType());
                specs.convert(physicalModelFunction.getUuid(), physicalModelDataStandardMap);
                dataAndType.setSpecs(specs);
            }
            inputData.setDataType(dataAndType);
            inputDataList.add(inputData);
        }
        Map<String, List<PhysicalModelFunction>> childStructFunctionMap = childStructFunctionList.parallelStream().collect(Collectors.groupingBy(PhysicalModelFunction::getParentId));
        for (PhysicalModelFunction physicalModelFunction : outputStructFunctionList) {
            if (childStructFunctionMap.get(physicalModelFunction.getUuid()) == null) {
                continue;
            }
            InOutputData outputData = new InOutputData();
            outputData.setIdentifier(physicalModelFunction.getIdentifier());
            outputData.setName(physicalModelFunction.getName());
            List<StructSpecs> structSpecsList = new ArrayList<>();
            DataAndType dataAndType = new DataAndType();
            dataAndType.setType(physicalModelFunction.getDataType());
            for (PhysicalModelFunction childFunction : childStructFunctionMap.get(physicalModelFunction.getUuid())) {
                StructSpecs specs = new StructSpecs();
                specs.setIdentifier(childFunction.getIdentifier());
                specs.setName(childFunction.getName());
                DataAndType childDataAndType = new DataAndType();
                Specs childSpecs = SpecsFactory.getInvokeSpecs(childFunction.getDataType());
                childSpecs.convert(childFunction.getUuid(), physicalModelDataStandardMap);
                childDataAndType.setSpecs(specs);
                specs.setDataType(childDataAndType);
                structSpecsList.add(specs);
            }
            dataAndType.setSpecs(structSpecsList);
            outputData.setDataType(dataAndType);
            outputDataList.add(outputData);
        }
        for (PhysicalModelFunction physicalModelFunction : inputStructFunctionList) {
            if (childStructFunctionMap.get(physicalModelFunction.getUuid()) == null) {
                continue;
            }
            InOutputData inputData = new InOutputData();
            inputData.setIdentifier(physicalModelFunction.getIdentifier());
            inputData.setName(physicalModelFunction.getName());
            List<StructSpecs> structSpecsList = new ArrayList<>();
            DataAndType dataAndType = new DataAndType();
            dataAndType.setType(physicalModelFunction.getDataType());
            for (PhysicalModelFunction childFunction : childStructFunctionMap.get(physicalModelFunction.getUuid())) {
                StructSpecs specs = new StructSpecs();
                specs.setIdentifier(childFunction.getIdentifier());
                specs.setName(childFunction.getName());
                DataAndType childDataAndType = new DataAndType();
                Specs childSpecs = SpecsFactory.getInvokeSpecs(childFunction.getDataType());
                childSpecs.convert(childFunction.getUuid(), physicalModelDataStandardMap);
                childDataAndType.setSpecs(specs);
                specs.setDataType(childDataAndType);
                structSpecsList.add(specs);
            }
            dataAndType.setSpecs(structSpecsList);
            inputData.setDataType(dataAndType);
            inputDataList.add(inputData);
        }
        if (ThingModelJsonType.events.equals(type)) {
            EventModel convertModel = (EventModel) model;
            convertModel.setOutputData(outputDataList);
        } else if (ThingModelJsonType.services.equals(type)) {
            ServiceModel convertModel = (ServiceModel) model;
            convertModel.setOutputData(outputDataList);
            convertModel.setInputData(inputDataList);
        }
    }

    private PropertiesModel buildModelProperties(PPhysicalModelFunction pFunction) {
        PropertiesModel properties = new PropertiesModel();
        properties.setType(DataType.valueOf(pFunction.getDataType()));
        properties.setDetails(pFunction.getDetails());
        properties.setName(pFunction.getName());
        properties.setIdentifier(pFunction.getIdentifier());
        properties.setRwFlag(pFunction.getRwFlag());
        return properties;
    }

    private EventModel buildModelEvent(PPhysicalModelFunction pFunction) {
        EventModel eventModel = new EventModel();
        if (StringUtils.isNotBlank(pFunction.getDataType())){
            eventModel.setType(DataType.valueOf(pFunction.getDataType()));
        }
        eventModel.setDetails(pFunction.getDetails());
        eventModel.setName(pFunction.getName());
        eventModel.setIdentifier(pFunction.getIdentifier());
        eventModel.setEventType(EventType.valueOf(pFunction.getEventType()));
        return eventModel;
    }

    private ServiceModel buildModelService(PPhysicalModelFunction pFunction) {
        ServiceModel serviceModel = new ServiceModel();
        if (StringUtils.isNotBlank(pFunction.getDataType())){
            serviceModel.setType(DataType.valueOf(pFunction.getDataType()));
        }
        serviceModel.setDetails(pFunction.getDetails());
        serviceModel.setName(pFunction.getName());
        serviceModel.setIdentifier(pFunction.getIdentifier());
        serviceModel.setCallType(CallType.valueOf(pFunction.getCallType()));
        return serviceModel;
    }

    private List<PhysicalModelFunction> queryStructFunction(String tenantId, List<String> parentIds) {
        List<PhysicalModelFunction> functionList = new ArrayList<>();
        if (CollectionUtils.isEmpty(parentIds)) {
            return functionList;
        }
        PhysicalModelFunctionFilter filter = new PhysicalModelFunctionFilter();
        filter.setTenantIdEq(tenantId);
        filter.setParentIdIn(parentIds);
        List<PPhysicalModelFunction> query = functionDao.query(filter);
        if (CollectionUtils.isNotEmpty(query)) {
            for (PPhysicalModelFunction function : query) {
                PhysicalModelFunction physicalModelFunction = new PhysicalModelFunction();
                BeanUtils.copyProperties(function, physicalModelFunction);
                physicalModelFunction.setDataType(DataType.valueOf(function.getDataType()));
                functionList.add(physicalModelFunction);
            }
        }
        return functionList;
    }

    private List<PPhysicalModelFunction> queryChildFunction(String tenantId, List<String> parentIds) {
        List<PPhysicalModelFunction> pPhysicalModelFunctions = new ArrayList<>();
        if (CollectionUtils.isEmpty(parentIds)) {
            return pPhysicalModelFunctions;
        }
        PhysicalModelFunctionFilter filter = new PhysicalModelFunctionFilter();
        filter.setTenantIdEq(tenantId);
        filter.setParentIdIn(parentIds);
        List<PPhysicalModelFunction> query = functionDao.query(filter);
        if (CollectionUtils.isNotEmpty(query)) {
            return query;
        }
        return pPhysicalModelFunctions;
    }


    //属性物模型的添加
    private void savePropertie(String tenantId, ModelProperties properties, String modelModuleId, String operator) {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PPhysicalModelFunction pFunction = new PPhysicalModelFunction();
            pFunction.setTenantId(tenantId);
            pFunction.setDataType(properties.getType().name());
            pFunction.setName(properties.getName());
            pFunction.setModelModuleId(modelModuleId);
            pFunction.setIdentifier(properties.getIdentifier());
            pFunction.setType(ThingModelJsonType.properties.name());
            pFunction.setRwFlag(properties.getRwFlag());
            pFunction.setDetails(properties.getDetails());
            pFunction.setRequired(false);
            pFunction.onCreated(operator);
            DataType type = null;
            Map dataTypeMap = JSON.parseObject(properties.getDataType(), Map.class);
            Map specsMap=null;
            if (DataType.ARRAY.equals(properties.getType())) {
                if (dataTypeMap.get("specs")!=null){
                    specsMap = JSON.parseObject(dataTypeMap.get("specs").toString(), Map.class);
                    pFunction.setArraySize(Integer.parseInt(specsMap.get("size").toString()));
                    Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                    pFunction.setArrayType(itemMap.get("type").toString());
                    type=DataType.valueOf(itemMap.get("type").toString());
                }
            }
            functionDao.save(pFunction);
            if (DataType.STRUCT.equals(properties.getType())) {
                List specs = (List) dataTypeMap.get("specs");
                for (Object spec : specs) {
                    saveStructSpec(tenantId, pFunction.getUuid(), spec.toString(), operator);
                }
            } else if (DataType.ARRAY.equals(properties.getType())) {
                if (DataType.STRUCT.equals(type)) {
                    if (specsMap!=null){
                        Map itemMap = JSON.parseObject(specsMap.get("item").toString(), Map.class);
                        List specs = (List) itemMap.get("specs");
                        for (Object spec : specs) {
                            saveStructSpec(tenantId, pFunction.getUuid(), spec.toString(), operator);
                        }
                    }
                }
            } else {
                saveDataStandard(tenantId, properties.getType(), pFunction.getUuid(), dataTypeMap.get("specs").toString(), operator);
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
            pFunction.setType(ThingModelJsonType.services.name());
            pFunction.setDetails(service.getDetails());
            pFunction.setRequired(true);
            pFunction.setCallType(service.getCallType().name());
            pFunction.onCreated(operator);
            functionDao.save(pFunction);
            if (CollectionUtils.isNotEmpty(service.getOutputParams())) {
                //保存输入参数
                saveIOParm(tenantId, pFunction.getUuid(), DirectionType.PARAM_OUTPUT, operator, service.getOutputParams());
            }
            if (CollectionUtils.isNotEmpty(service.getInputParams())) {
                //保存输出参数
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
            pFunction.setType(ThingModelJsonType.events.name());
            pFunction.setDetails(event.getDetails());
            pFunction.setRequired(true);
            pFunction.onCreated(operator);
            pFunction.setEventType(event.getEventType().name());
            functionDao.save(pFunction);
            if (CollectionUtils.isNotEmpty(event.getOutputParams())) {
                //保存输出参数
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
                pFunction.setDirection(ioType.name());
                pFunction.onCreated(operator);
                Map map = JSON.parseObject(param.getDataType(), Map.class);
                DataType type = DataType.valueOf(map.get("type").toString());
                pFunction.setDataType(type.name());
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
            pFunction.setDataType(map.get("type").toString());
            functionDao.save(pFunction);
            saveDataStandard(tenantId, DataType.valueOf(map.get("type").toString()), pFunction.getUuid(), map.get("specs").toString(), operator);
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


