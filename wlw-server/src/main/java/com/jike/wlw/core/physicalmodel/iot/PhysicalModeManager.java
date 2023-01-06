/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月05日 14:49 - ASUS - 创建。
 */
package com.jike.wlw.core.physicalmodel.iot;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import io.micrometer.core.instrument.util.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 *
 *
 * @author rs
 * @since 1.0
 */
public class PhysicalModeManager {
    @Autowired
    private Environment env;

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config();
        // 您的 AccessKey ID
        config.setAccessKeyId(accessKeyId);
        // 您的 AccessKey Secret
        config.setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        return new Client(config);
    }

         //CreateThingModel
    public CreateThingModelResponse createThingModel(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CreateThingModelRequest request=new CreateThingModelRequest();
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setThingModelJson(new JSONObject(model.getThingModelJson()).toString());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setFunctionBlockName(model.getFunctionBlockName());
        CreateThingModelResponse response = client.createThingModel(request);
        return response;
    }
    //UpdateThingModel
    public UpdateThingModelResponse updateThingModel(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        UpdateThingModelRequest request=new UpdateThingModelRequest();
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setIdentifier(model.getIdentifier());
        request.setFunctionBlockName(model.getFunctionBlockName());
        request.setThingModelJson(new JSONObject(model.getThingModelJson()).toString());
        UpdateThingModelResponse response = client.updateThingModel(request);
        return response;
    }
    //QueryThingModel
    public QueryThingModelResponse queryThingModel(PhysicalModelManagerRq model) throws Exception {
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryThingModelRequest request=new QueryThingModelRequest();
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setModelVersion(model.getModelVersion());
        request.setResourceGroupId(model.getResourceGroupId());
        QueryThingModelResponse response = client.queryThingModel(request);
        return response;
    }
    //CopyThingModel
    public CopyThingModelResponse copyThingModel(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getSourceProductKey())||StringUtils.isBlank(model.getTargetProductKey())){
            throw new IllegalAccessException("复制的物模型所属产品或目标物模型productKey不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CopyThingModelRequest request=new CopyThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setSourceModelVersion(model.getModelVersion());
        request.setSourceProductKey(model.getSourceProductKey());
        request.setTargetProductKey(model.getTargetProductKey());
        CopyThingModelResponse response = client.copyThingModel(request);
        return response;
    }

    //DeleteThingModel
    public DeleteThingModelResponse deleteThingModel(PhysicalModelManagerRq model) throws Exception{
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        DeleteThingModelRequest request=new DeleteThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setServiceIdentifier(model.getServiceIdentifier());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setEventIdentifier(model.getEventIdentifier());
        DeleteThingModelResponse response = client.deleteThingModel(request);
        return response;
    }
    //ListThingTemplates
    public ListThingTemplatesResponse listThingTemplates(PhysicalModelManagerRq model) throws Exception {
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        ListThingTemplatesRequest request=new ListThingTemplatesRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        ListThingTemplatesResponse response = client.listThingTemplates(request);
        return response;
    }
    //GetThingTemplate
    public GetThingTemplateResponse getThingTemplate(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getCategoryKey())){
            throw new IllegalAccessException("查询的品类的标识符不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingTemplateRequest request=new GetThingTemplateRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setCategoryKey(model.getCategoryKey());
        GetThingTemplateResponse response = client.getThingTemplate(request);
        return response;
    }

    //GetThingModelTsl
    public GetThingModelTslResponse getThingModelTsl(PhysicalModelManagerRq model) throws Exception {
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingModelTslRequest request=new GetThingModelTslRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setSimple(model.isSimple());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        GetThingModelTslResponse response = client.getThingModelTsl(request);
        return response;
    }
    //QueryThingModelPublished
    public QueryThingModelPublishedResponse queryThingModelPublished(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryThingModelPublishedRequest request=new QueryThingModelPublishedRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        QueryThingModelPublishedResponse response = client.queryThingModelPublished(request);
        return response;
    }
    //GetThingModelTslPublished
    public GetThingModelTslPublishedResponse getThingModelTslPublished(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingModelTslPublishedRequest request=new GetThingModelTslPublishedRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setSimple(model.isSimple());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        request.setResourceGroupId(model.getResourceGroupId());
        GetThingModelTslPublishedResponse response = client.getThingModelTslPublished(request);
        return response;
    }
}
