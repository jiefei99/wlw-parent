/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月05日 14:49 - ASUS - 创建。
 */
package com.jike.wlw.core.physicalmodel.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.CopyThingModelRequest;
import com.aliyun.iot20180120.models.CopyThingModelResponse;
import com.aliyun.iot20180120.models.CreateThingModelRequest;
import com.aliyun.iot20180120.models.CreateThingModelResponse;
import com.aliyun.iot20180120.models.DeleteThingModelRequest;
import com.aliyun.iot20180120.models.DeleteThingModelResponse;
import com.aliyun.iot20180120.models.GetThingModelTslPublishedRequest;
import com.aliyun.iot20180120.models.GetThingModelTslPublishedResponse;
import com.aliyun.iot20180120.models.GetThingModelTslRequest;
import com.aliyun.iot20180120.models.GetThingModelTslResponse;
import com.aliyun.iot20180120.models.GetThingTemplateRequest;
import com.aliyun.iot20180120.models.GetThingTemplateResponse;
import com.aliyun.iot20180120.models.ListThingModelVersionRequest;
import com.aliyun.iot20180120.models.ListThingModelVersionResponse;
import com.aliyun.iot20180120.models.ListThingTemplatesRequest;
import com.aliyun.iot20180120.models.ListThingTemplatesResponse;
import com.aliyun.iot20180120.models.PublishThingModelRequest;
import com.aliyun.iot20180120.models.PublishThingModelResponse;
import com.aliyun.iot20180120.models.QueryThingModelPublishedRequest;
import com.aliyun.iot20180120.models.QueryThingModelPublishedResponse;
import com.aliyun.iot20180120.models.QueryThingModelRequest;
import com.aliyun.iot20180120.models.QueryThingModelResponse;
import com.aliyun.iot20180120.models.UpdateThingModelRequest;
import com.aliyun.iot20180120.models.UpdateThingModelResponse;
import com.aliyun.teaopenapi.models.Config;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.service.physicalmodel.PhysicalModelCopyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPubTslGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishQueryRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelTslGetRq;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author rs
 * @since 1.0
 */

@Slf4j
@Service
public class PhysicalModelManager {

    @Autowired
    private AliIotClient client;
    @Autowired
    private Environment env;


    //CreateThingModel
    public CreateThingModelResponse createThingModel(PhysicalModelCreateRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())) {
            throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CreateThingModelRequest request = new CreateThingModelRequest();
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setThingModelJson(model.getThingModelJson());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setFunctionBlockName(model.getFunctionBlockName());
        CreateThingModelResponse response = client.createThingModel(request);
        System.out.println("物模型新增功能：" + JSON.toJSONString(response));
        return response;
    }

    //UpdateThingModel
    public UpdateThingModelResponse updateThingModel(PhysicalModelModifyRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())) {
            throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        UpdateThingModelRequest request = new UpdateThingModelRequest();
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setIdentifier(model.getIdentifier());
        request.setFunctionBlockName(model.getFunctionBlockName());
        request.setThingModelJson(new JSONObject(model.getThingModelJson()).toString());
        UpdateThingModelResponse response = client.updateThingModel(request);
        System.out.println("指定更新物模型单个功能：" + JSON.toJSONString(response));
        return response;
    }

    //QueryThingModel
    public QueryThingModelResponse queryThingModel(PhysicalModelGetRq model) throws Exception {
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryThingModelRequest request = new QueryThingModelRequest();
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setModelVersion(model.getModelVersion());
        request.setResourceGroupId(model.getResourceGroupId());
        QueryThingModelResponse response = client.queryThingModel(request);
        System.out.println("查看指定产品物模型中的功能定义详情：" + JSON.toJSONString(response));
        return response;
    }

    //CopyThingModel
    public CopyThingModelResponse copyThingModel(PhysicalModelCopyRq model) throws Exception {
        if (StringUtils.isBlank(model.getSourceProductKey()) || StringUtils.isBlank(model.getTargetProductKey())) {
            throw new IllegalAccessException("复制的物模型所属产品或目标物模型productKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CopyThingModelRequest request = new CopyThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setSourceModelVersion(model.getSourceModelVersion());
        request.setSourceProductKey(model.getSourceProductKey());
        request.setTargetProductKey(model.getTargetProductKey());
        CopyThingModelResponse response = client.copyThingModel(request);
        System.out.println("复制指定产品的物模型到目标产品：" + JSON.toJSONString(response));
        return response;
    }

    //DeleteThingModel
    public DeleteThingModelResponse deleteThingModel(PhysicalModelDelRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())) {
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
        if (CollectionUtils.isNotEmpty(model.getServiceIdentifier()) && model.getServiceIdentifier().size() > 10) {
            throw new IllegalAccessException("最多传入10个服务标识符");
        }
        if (CollectionUtils.isNotEmpty(model.getEventIdentifier()) && model.getEventIdentifier().size() > 10) {
            throw new IllegalAccessException("最多传入10个事件标识符");
        }
        if (CollectionUtils.isNotEmpty(model.getPropertyIdentifier()) && model.getPropertyIdentifier().size() > 10) {
            throw new IllegalAccessException("最多传入10个属性标识符");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        DeleteThingModelRequest request = new DeleteThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setServiceIdentifier(model.getServiceIdentifier());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setEventIdentifier(model.getEventIdentifier());
        request.setPropertyIdentifier(model.getPropertyIdentifier());
        DeleteThingModelResponse response = client.deleteThingModel(request);
        System.out.println("删除指定产品下物模型中的功能：" + JSON.toJSONString(response));
        return response;
    }

    //ListThingTemplates
    public ListThingTemplatesResponse listThingTemplates(String iotInstanceId) throws Exception {
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        ListThingTemplatesRequest request = new ListThingTemplatesRequest();
        request.setIotInstanceId(iotInstanceId);
        ListThingTemplatesResponse response = client.listThingTemplates(request);
        System.out.println("获取物联网平台预定义的标准产品品类列表：" + JSON.toJSONString(response));
        return response;
    }

    //GetThingTemplate
    public GetThingTemplateResponse getThingTemplate(String categoryKey, String resourceGroupId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(categoryKey)) {
            throw new IllegalAccessException("查询的品类的标识符不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingTemplateRequest request = new GetThingTemplateRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setResourceGroupId(resourceGroupId);
        request.setCategoryKey(categoryKey);
        GetThingTemplateResponse response = client.getThingTemplate(request);
        System.out.println("指定品类的物模型信息：" + JSON.toJSONString(response));
        return response;
    }

    //GetThingModelTsl
    public GetThingModelTslResponse getThingModelTsl(PhysicalModelTslGetRq model) throws Exception {
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingModelTslRequest request = new GetThingModelTslRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setSimple(model.isSimple());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        GetThingModelTslResponse response = client.getThingModelTsl(request);
        System.out.println("查询指定产品的物模型TSL：" + JSON.toJSONString(response));
        return response;
    }

    //QueryThingModelPublished
    public QueryThingModelPublishedResponse queryThingModelPublished(PhysicalModelPublishQueryRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())) {
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryThingModelPublishedRequest request = new QueryThingModelPublishedRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        QueryThingModelPublishedResponse response = client.queryThingModelPublished(request);
        System.out.println("调用该接口查看指定产品的已发布物模型中的功能定义详情：" + JSON.toJSONString(response));
        return response;
    }

    //GetThingModelTslPublished
    public GetThingModelTslPublishedResponse getThingModelTslPublished(PhysicalModelPubTslGetRq model) throws
            Exception {
        if (StringUtils.isBlank(model.getProductKey())) {
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingModelTslPublishedRequest request = new GetThingModelTslPublishedRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setSimple(model.isSimple());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        request.setResourceGroupId(model.getResourceGroupId());
        GetThingModelTslPublishedResponse response = client.getThingModelTslPublished(request);
        System.out.println("指定产品的已发布物模型TSL：" + JSON.toJSONString(response));
        return response;
    }

    //    ListThingModelVersion
    public ListThingModelVersionResponse listThingModelVersion(String productKey, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)) {
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        ListThingModelVersionRequest request = new ListThingModelVersionRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setProductKey(productKey);
        ListThingModelVersionResponse response = client.listThingModelVersion(request);
        System.out.println("获取指定产品的物模型版本：" + JSON.toJSONString(response));
        return response;
    }

    //    PublishThingModel
    public PublishThingModelResponse publishThingModel(PhysicalModelPublishRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())) {
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        PublishThingModelRequest request = new PublishThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setProductKey(model.getProductKey());
        request.setModelVersion(model.getModelVersion());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setDescription(model.getDescription());
        PublishThingModelResponse response = client.publishThingModel(request);
        System.out.println("发布指定产品的物模型：" + JSON.toJSONString(response));
        return response;
    }
}

