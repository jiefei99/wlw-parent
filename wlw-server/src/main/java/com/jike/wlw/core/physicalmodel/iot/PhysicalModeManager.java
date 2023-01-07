/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月05日 14:49 - ASUS - 创建。
 */
package com.jike.wlw.core.physicalmodel.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.*;

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

    public static void main(String[] args) {
        PhysicalModelManagerRq physicalModelManagerRq=new PhysicalModelManagerRq();
//        physicalModelManagerRq.setResourceGroupId("rg-acfm4l5tcwdwer12as");
//        List<String> eventIdentifier= Arrays.asList("rsTest1","rsTest2");
//        physicalModelManagerRq.setEventIdentifier(eventIdentifier);
//        List<String> serviceIdentifier= Arrays.asList("rsTest3","rsTest4");
//        physicalModelManagerRq.setServiceIdentifier(serviceIdentifier);
        physicalModelManagerRq.setProductKey("a1VczzGUHh8");
//        physicalModelManagerRq.setIotInstanceId("iot-cn-0pp1n8t23ae");
//        List<String> propertyIdentifier= Arrays.asList("rsTest5","rsTest6");
//        physicalModelManagerRq.setPropertyIdentifier(propertyIdentifier);
//        Map<String,Object> thingModelMap=new HashMap();
//        thingModelMap.put("required",false);
//        thingModelMap.put("customFlag",false);
//        physicalModelManagerRq.setThingModelJson(thingModelMap);
//        physicalModelManagerRq.setCategoryKey("Lighting");
//        physicalModelManagerRq.setPropertyId();
//        physicalModelManagerRq.setIdentifier("SimCardType");
//        physicalModelManagerRq.setModelVersion("v1.0.0");
//        physicalModelManagerRq.setSourceProductKey("a1GgN502dxa");
//        physicalModelManagerRq.setTargetProductKey("a1VczzGUHh8");
//        physicalModelManagerRq.setSourceModelVersion("v1.0.0");
        physicalModelManagerRq.setSimple(false);
        physicalModelManagerRq.setModelVersion("v1.0.0");
        PhysicalModeManager physicalModeManager=new PhysicalModeManager();
        try {
            physicalModeManager.getThingModelTslPublished(physicalModelManagerRq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
         //CreateThingModel
    public CreateThingModelResponse createThingModel(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CreateThingModelRequest request=new CreateThingModelRequest();
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setThingModelJson(new JSONObject(model.getThingModelJson()).toString());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setFunctionBlockName(model.getFunctionBlockName());
        CreateThingModelResponse response = client.createThingModel(request);
        System.out.println("物模型新增功能："+JSON.toJSONString(response));
        return response;
    }
    //UpdateThingModel
    public UpdateThingModelResponse updateThingModel(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("物模型产品的ProductKey属性值不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        UpdateThingModelRequest request=new UpdateThingModelRequest();
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setIdentifier(model.getIdentifier());
        request.setFunctionBlockName(model.getFunctionBlockName());
        request.setThingModelJson(new JSONObject(model.getThingModelJson()).toString());
        UpdateThingModelResponse response = client.updateThingModel(request);
        System.out.println("指定更新物模型单个功能："+JSON.toJSONString(response));
        return response;
    }
    //QueryThingModel
    public QueryThingModelResponse queryThingModel(PhysicalModelManagerRq model) throws Exception {
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryThingModelRequest request=new QueryThingModelRequest();
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        request.setModelVersion(model.getModelVersion());
        request.setResourceGroupId(model.getResourceGroupId());
        QueryThingModelResponse response = client.queryThingModel(request);
        System.out.println("查看指定产品物模型中的功能定义详情："+JSON.toJSONString(response));
        return response;
    }

    //CopyThingModel
    public CopyThingModelResponse copyThingModel(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())||StringUtils.isBlank(model.getTargetProductKey())){
            throw new IllegalAccessException("复制的物模型所属产品或目标物模型productKey不能为空");
        }
        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CopyThingModelRequest request=new CopyThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setSourceModelVersion(model.getModelVersion());
        request.setSourceProductKey(model.getProductKey());
        request.setTargetProductKey(model.getTargetProductKey());
        CopyThingModelResponse response = client.copyThingModel(request);
        System.out.println("复制指定产品的物模型到目标产品："+JSON.toJSONString(response));
        return response;
    }

    //DeleteThingModel
    public DeleteThingModelResponse deleteThingModel(PhysicalModelManagerRq model) throws Exception{
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
        if (CollectionUtils.isNotEmpty(model.getServiceIdentifier())&&model.getServiceIdentifier().size()>10){
            throw new IllegalAccessException("最多传入10个服务标识符");
        }
        if (CollectionUtils.isNotEmpty(model.getEventIdentifier())&&model.getEventIdentifier().size()>10){
            throw new IllegalAccessException("最多传入10个事件标识符");
        }
        if (CollectionUtils.isNotEmpty(model.getPropertyIdentifier())&&model.getPropertyIdentifier().size()>10){
            throw new IllegalAccessException("最多传入10个属性标识符");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        DeleteThingModelRequest request=new DeleteThingModelRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setServiceIdentifier(model.getServiceIdentifier());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setProductKey(model.getProductKey());
        request.setEventIdentifier(model.getEventIdentifier());
        request.setPropertyIdentifier(model.getPropertyIdentifier());
        DeleteThingModelResponse response = client.deleteThingModel(request);
        System.out.println("删除指定产品下物模型中的功能："+JSON.toJSONString(response));
        return response;
    }
    //ListThingTemplates
    public ListThingTemplatesResponse listThingTemplates(PhysicalModelManagerRq model) throws Exception {
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        ListThingTemplatesRequest request=new ListThingTemplatesRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        ListThingTemplatesResponse response = client.listThingTemplates(request);
        System.out.println("获取物联网平台预定义的标准产品品类列表："+JSON.toJSONString(response));
        return response;
    }
    //GetThingTemplate
    public GetThingTemplateResponse getThingTemplate(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getCategoryKey())){
            throw new IllegalAccessException("查询的品类的标识符不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingTemplateRequest request=new GetThingTemplateRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setCategoryKey(model.getCategoryKey());
        GetThingTemplateResponse response = client.getThingTemplate(request);
        System.out.println("指定品类的物模型信息："+JSON.toJSONString(response));
        return response;
    }

    //GetThingModelTsl
    public GetThingModelTslResponse getThingModelTsl(PhysicalModelManagerRq model) throws Exception {
        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingModelTslRequest request=new GetThingModelTslRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setSimple(model.isSimple());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        GetThingModelTslResponse response = client.getThingModelTsl(request);
        System.out.println("查询指定产品的物模型TSL："+JSON.toJSONString(response));
        return response;
    }
    //QueryThingModelPublished
    public QueryThingModelPublishedResponse queryThingModelPublished(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryThingModelPublishedRequest request=new QueryThingModelPublishedRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setResourceGroupId(model.getResourceGroupId());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        QueryThingModelPublishedResponse response = client.queryThingModelPublished(request);
        System.out.println("指定产品的物模型扩展描述配置："+JSON.toJSONString(response));
        return response;
    }
    //GetThingModelTslPublished
    public GetThingModelTslPublishedResponse getThingModelTslPublished(PhysicalModelManagerRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("产品的ProductKey不能为空");
        }
        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        GetThingModelTslPublishedRequest request=new GetThingModelTslPublishedRequest();
        request.setIotInstanceId(model.getIotInstanceId());
        request.setFunctionBlockId(model.getFunctionBlockId());
        request.setSimple(model.isSimple());
        request.setModelVersion(model.getModelVersion());
        request.setProductKey(model.getProductKey());
        request.setResourceGroupId(model.getResourceGroupId());
        GetThingModelTslPublishedResponse response = client.getThingModelTslPublished(request);
        System.out.println("指定产品的已发布物模型TSL："+ JSON.toJSONString(response));
        return response;
    }
}
