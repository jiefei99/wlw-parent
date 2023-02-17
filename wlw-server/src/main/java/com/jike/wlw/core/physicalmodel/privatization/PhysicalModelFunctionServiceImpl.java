package com.jike.wlw.core.physicalmodel.privatization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelFunction;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.EventType;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelFunctionCreateRq;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @Override
    public void create(String tenantId, PhysicalModelFunctionCreateRq createRq, String operator) throws BusinessException {
        if (createRq == null) {
            throw new BusinessException("物模型功能参数不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getModelDeviceId())) {
            throw new BusinessException("功能所属物模型Id不能为空");
        }
        if (StringUtils.isBlank(createRq.getThingModelJson())) {
            throw new BusinessException("物模型功能不能为空");
        }
        PPhysicalModelFunction function = new PPhysicalModelFunction();
        JSONObject jsonObject = JSON.parseObject(createRq.getThingModelJson());
        Set<String> thingModelJsonSet = jsonObject.keySet();
        Set<String> thingModelTypeSet = new HashSet<String>() {{
            add(ThingModelJsonType.services.toString());
            add(ThingModelJsonType.events.toString());
            add(ThingModelJsonType.properties.toString());
        }};
        //取交集
        thingModelTypeSet.retainAll(thingModelJsonSet);
        if (CollectionUtils.isEmpty(thingModelTypeSet)) {
            return;
        }
        try {
            for (String type : thingModelTypeSet) {
                List funcitonList = (List) jsonObject.get(type);
                for (Object o : funcitonList) {
                    JSONObject detailJson = JSON.parseObject(o.toString());
                    if (ThingModelJsonType.properties.equals(type)) {
                        function.setRwFlag(detailJson.getString("rwFlag"));
                    } else if (ThingModelJsonType.events.equals(type)) {
                        function.setEventType(EventType.valueOf(detailJson.getString("eventType")));
                        function.setMethod(detailJson.getString("method"));
                    } else if (ThingModelJsonType.services.equals(type)) {
                        function.setCallType(CallType.valueOf(detailJson.getString("callType")));
                        function.setMethod(detailJson.getString("method"));
                    }
                    function.setTenantId(tenantId);
                    function.setModelDeviceId(createRq.getModelDeviceId());
                    function.setName(detailJson.getString("name"));
                    function.setType(type);
                    function.setDetails(detailJson.getString("description"));
                    function.setIdentifier(detailJson.getString("identifier"));
                    functionDao.save(function);
                    // 根据id存储data标准表
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}


