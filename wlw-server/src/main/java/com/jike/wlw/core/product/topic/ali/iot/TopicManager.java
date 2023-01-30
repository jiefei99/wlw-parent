/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月06日 14:55 - ASUS - 创建。
 */
package com.jike.wlw.core.product.topic.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.CreateProductTopicRequest;
import com.aliyun.iot20180120.models.CreateProductTopicResponse;
import com.aliyun.iot20180120.models.DeleteProductTopicRequest;
import com.aliyun.iot20180120.models.DeleteProductTopicResponse;
import com.aliyun.iot20180120.models.QueryProductTopicRequest;
import com.aliyun.iot20180120.models.QueryProductTopicResponse;
import com.aliyun.iot20180120.models.UpdateProductTopicRequest;
import com.aliyun.iot20180120.models.UpdateProductTopicResponse;
import com.aliyun.teaopenapi.models.Config;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.service.product.topic.TopicCreateRq;
import com.jike.wlw.service.product.topic.TopicFilter;
import com.jike.wlw.service.product.topic.TopicModifyRq;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 * @author rs
 * @since 1.0
 */

@Slf4j
@Service
public class TopicManager {

    @Autowired
    private AliIotClient client;

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


    //CreateProductTopic
    public CreateProductTopicResponse createProductTopic(TopicCreateRq topic) throws Exception{
        if (topic.getOperation()==null){
            throw new IllegalAccessException("设备对该Topic类的操作权限不能为空");
        }
        if (StringUtils.isBlank(topic.getProductKey())){
            throw new IllegalAccessException("Topic类的productKey不能为空");
        }
        if (StringUtils.isBlank(topic.getTopicShortName())){
            throw new IllegalAccessException("设置Topic类的自定义类目名称不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateProductTopicRequest request=new CreateProductTopicRequest();
        request.setProductKey(topic.getProductKey());
        request.setDesc(topic.getDesc());
        request.setOperation(topic.getOperation().toString());
        request.setTopicShortName(topic.getTopicShortName());
        request.setIotInstanceId(topic.getIotInstanceId());
        CreateProductTopicResponse response = client.createProductTopic(request);
        System.out.println("产品创建自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
    //UpdateProductTopic
    public UpdateProductTopicResponse updateProductTopic(TopicModifyRq topic) throws Exception {
        if (topic.getOperation()==null){
            throw new IllegalAccessException("设备对该Topic类的操作权限不能为空");
        }
        if (StringUtils.isBlank(topic.getId())){
            throw new IllegalAccessException("修改的Topic类的ID不能为空");
        }
        if (StringUtils.isBlank(topic.getTopicShortName())){
            throw new IllegalAccessException("设置Topic类的自定义类目名称不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateProductTopicRequest request=new UpdateProductTopicRequest();
        request.setDesc(topic.getDesc());
        request.setIotInstanceId(topic.getIotInstanceId());
        request.setOperation(topic.getOperation().toString());
        request.setTopicShortName(topic.getTopicShortName());
        request.setTopicId(topic.getId());
        UpdateProductTopicResponse response = client.updateProductTopic(request);
        System.out.println("修改产品自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
    //QueryProductTopic
    public QueryProductTopicResponse queryProductTopic(String productKey,String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)){
            throw new IllegalAccessException("Topic类的productKey不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryProductTopicRequest request=new QueryProductTopicRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setProductKey(productKey);
        QueryProductTopicResponse response = client.queryProductTopic(request);
        System.out.println("查询指定产品的自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteProductTopic
    public DeleteProductTopicResponse deleteProductTopic(String topicId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(topicId)){
            throw new IllegalAccessException("修改的Topic类的ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteProductTopicRequest request=new DeleteProductTopicRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setTopicId(topicId);
        DeleteProductTopicResponse response = client.deleteProductTopic(request);
        System.out.println("删除产品自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
}
