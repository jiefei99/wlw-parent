package com.jike.wlw.core.physicalmodel.ali;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.iot20180120.models.GetThingModelTslPublishedResponse;
import com.aliyun.iot20180120.models.GetThingModelTslPublishedResponseBody.GetThingModelTslPublishedResponseBodyData;
import com.aliyun.iot20180120.models.GetThingModelTslResponse;
import com.aliyun.iot20180120.models.GetThingModelTslResponseBody.GetThingModelTslResponseBodyData;
import com.aliyun.iot20180120.models.GetThingTemplateResponse;
import com.aliyun.iot20180120.models.ListThingModelVersionResponse;
import com.aliyun.iot20180120.models.ListThingModelVersionResponseBody.ListThingModelVersionResponseBodyDataModelVersions;
import com.aliyun.iot20180120.models.ListThingTemplatesResponse;
import com.aliyun.iot20180120.models.ListThingTemplatesResponseBody.ListThingTemplatesResponseBodyData;
import com.aliyun.iot20180120.models.QueryThingModelPublishedResponse;
import com.aliyun.iot20180120.models.QueryThingModelResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.core.physicalmodel.ali.iot.PhysicalModelManager;
import com.jike.wlw.service.physicalmodel.ModelVersion;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelCopyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPubTslGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishQueryRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelTsl;
import com.jike.wlw.service.physicalmodel.PhysicalModelTslGetRq;
import com.jike.wlw.service.physicalmodel.ali.PhysicalModelManagerService;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiModel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: AliPhysicalModeManagerServiceImpl
 * @Author RS
 * @Date: 2023/2/15 16:57
 * @Version 1.0
 */
@Slf4j
@RestController("modelServiceAliImpl")
@ApiModel("阿里物模型服务实现")
public class PhysicalModelManagerServiceImpl implements PhysicalModelManagerService {
    @Autowired
    private PhysicalModelManager manager;

    @Override
    public void create(String tenantId, PhysicalModelCreateRq createRq, String operator) throws BusinessException {
        try {
            if (createRq == null) {
                throw new IllegalAccessException("创建物模型参数不能为空");
            }
            if (StringUtils.isBlank(createRq.getProductKey())) {
                throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
            }
            manager.createThingModel(createRq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PhysicalModel get(String tenantId, PhysicalModelGetRq modelGetRq) throws BusinessException {
        PhysicalModel model = new PhysicalModel();
        try {
            if (modelGetRq == null) {
                throw new IllegalAccessException("创建物模型参数不能为空");
            }
            if (StringUtils.isBlank(modelGetRq.getProductKey())) {
                throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
            }
            QueryThingModelResponse response = manager.queryThingModel(modelGetRq);
            if (StringUtils.isBlank(response.getBody().getData().getThingModelJson())) {
                return model;
            }
            model = JSON.parseObject(response.getBody().getData().getThingModelJson(), PhysicalModel.class);
            model.setProductKey(modelGetRq.getProductKey());
            model.setFunctionBlockId(modelGetRq.getFunctionBlockId());
            String ppk = JSON.parseObject(response.getBody().getData().getThingModelJson()).getString("_ppk");
            if (StringUtils.isNotBlank(ppk)){
                model.setVersion(JSON.parseObject(ppk).getString("version"));
                model.setDesc(JSON.parseObject(ppk).getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public List<ModelVersion> getVersion(String tenantId, String productKey, String iotInstanceId) throws BusinessException {
        List<ModelVersion> versionList= new ArrayList<>();
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
            }
            ListThingModelVersionResponse response = manager.listThingModelVersion(productKey, iotInstanceId);
            List<ListThingModelVersionResponseBodyDataModelVersions> modelVersions = response.getBody().getData().getModelVersions();
            if (CollectionUtils.isEmpty(modelVersions)){
                return versionList;
            }
            for (ListThingModelVersionResponseBodyDataModelVersions modelVersion : modelVersions) {
                ModelVersion version=new ModelVersion();
                version.setModelVersion(modelVersion.getModelVersion());
                version.setCreated(new Date(modelVersion.getGmtCreate()));
                version.setDescription(modelVersion.getDescription());
                versionList.add(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionList;
    }

    @Override
    public PhysicalModelTsl getTslPublished(String tenantId, PhysicalModelPubTslGetRq modelGetRq) throws BusinessException {
        PhysicalModelTsl modelTsl=new PhysicalModelTsl();
        try {
            if (modelGetRq == null) {
                throw new IllegalAccessException("创建物模型参数不能为空");
            }
            if (StringUtils.isBlank(modelGetRq.getProductKey())) {
                throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
            }
            GetThingModelTslPublishedResponse resp = manager.getThingModelTslPublished(modelGetRq);
            GetThingModelTslPublishedResponseBodyData data = resp.getBody().getData();
            modelTsl.setTslUri(data.getTslUri());
            modelTsl.setTslStr(data.getTslStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelTsl;
    }

    @Override
    public PhysicalModelTsl getTsl(String tenantId, PhysicalModelTslGetRq modelGetRq) throws BusinessException {
        PhysicalModelTsl modelTsl=new PhysicalModelTsl();
        try {
            if (modelGetRq == null) {
                throw new IllegalAccessException("创建物模型参数不能为空");
            }
            if (StringUtils.isBlank(modelGetRq.getProductKey())) {
                throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
            }
            GetThingModelTslResponse response = manager.getThingModelTsl(modelGetRq);
            GetThingModelTslResponseBodyData data = response.getBody().getData();
            modelTsl.setTslUri(data.getTslUri());
            modelTsl.setTslStr(data.getTslStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelTsl;
    }

    @Override
    public PhysicalModel getCategory(String tenantId, String categoryKey, String resourceGroupId, String iotInstanceId) throws BusinessException {
        PhysicalModel model=new PhysicalModel();
        try {
            if (StringUtils.isBlank(categoryKey)) {
                throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
            }
            GetThingTemplateResponse response = manager.getThingTemplate(categoryKey, resourceGroupId, iotInstanceId);
            if (StringUtils.isNotBlank(response.getBody().getThingModelJSON())){
                model = JSONObject.parseObject(response.getBody().getThingModelJSON(), PhysicalModel.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public void modify(String tenantId, PhysicalModelModifyRq modifyRq, String operator) throws BusinessException {
        try {
            if (modifyRq == null) {
                throw new IllegalAccessException("修改物模型参数不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getProductKey())) {
                throw new IllegalAccessException("产品的ProductKey不能为空");
            }
            manager.updateThingModel(modifyRq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String tenantId, PhysicalModelDelRq modelDelRq, String operator) throws BusinessException {
        try {
            if (modelDelRq == null) {
                throw new IllegalAccessException("删除物模型参数不能为空");
            }
            if (StringUtils.isBlank(modelDelRq.getProductKey())) {
                throw new IllegalAccessException("产品的ProductKey不能为空");
            }
            manager.deleteThingModel(modelDelRq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, String>> queryTemplates(String tenantId, String iotInstanceId) throws BusinessException {
        List<Map<String, String>> list=new ArrayList<>();
        try {
            ListThingTemplatesResponse response = manager.listThingTemplates(iotInstanceId);
            List<ListThingTemplatesResponseBodyData> data = response.getBody().getData();
            for (ListThingTemplatesResponseBodyData item : data) {
                Map<String,String> map =new HashMap<>();
                map.put(PhysicalModel.CATEGORYKEY,item.getCategoryKey());
                map.put(PhysicalModel.CATEGORYNAME,item.getCategoryName());
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public PhysicalModel queryPublish(String tenantId, PhysicalModelPublishQueryRq filter) throws BusinessException {
        PhysicalModel model=new PhysicalModel();
        try {
            if (filter == null) {
                throw new IllegalAccessException("查询物模型参数不能为空");
            }
            if (StringUtils.isBlank(filter.getProductKey())) {
                throw new IllegalAccessException("产品的ProductKey不能为空");
            }
            QueryThingModelPublishedResponse response = manager.queryThingModelPublished(filter);
            if (StringUtils.isNotBlank(response.getBody().getData().getThingModelJson())){
                model= JSONObject.parseObject(response.getBody().getData().getThingModelJson(), PhysicalModel.class);
                model.setProductKey(filter.getProductKey());
                String ppk = JSON.parseObject(response.getBody().getData().getThingModelJson()).getString("_ppk");
                if (StringUtils.isNotBlank(ppk)){
                    model.setVersion(JSON.parseObject(ppk).getString("version"));
                    model.setDesc(JSON.parseObject(ppk).getString("description"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public void copy(String tenantId, PhysicalModelCopyRq modifyRq, String operator) throws BusinessException {
        try {
            if (modifyRq == null) {
                throw new IllegalAccessException("复制物模型参数不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getSourceProductKey()) || StringUtils.isBlank(modifyRq.getTargetProductKey())) {
                throw new IllegalAccessException("复制的物模型所属产品或目标物模型productKey不能为空");
            }
            manager.copyThingModel(modifyRq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String tenantId, PhysicalModelPublishRq publishRq, String operator) throws BusinessException {
        try {
            if (publishRq == null) {
                throw new IllegalAccessException("发布物模型参数不能为空");
            }
            if (StringUtils.isBlank(publishRq.getProductKey())) {
                throw new IllegalAccessException("产品的ProductKey不能为空");
            }
            manager.publishThingModel(publishRq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


