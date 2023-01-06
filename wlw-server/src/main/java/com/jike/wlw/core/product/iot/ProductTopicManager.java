/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月06日 14:55 - ASUS - 创建。
 */
package com.jike.wlw.core.product.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
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
public class ProductTopicManager {

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
        ProductTopicRq productTopicRq=new ProductTopicRq();
//        productTopicRq.setProductKey("a1GgN502dxa");
//        productTopicRq.setOperation("ALL");
//        productTopicRq.setDesc("resubmit a test topic");
//        productTopicRq.setIotInstanceId();
//        productTopicRq.setTopicShortName("resubmit");
        productTopicRq.setTopicId("31823259");
        ProductTopicManager productTopicManager=new ProductTopicManager();
        try {
            productTopicManager.deleteProductTopic(productTopicRq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //CreateProductTopic
    public CreateProductTopicResponse createProductTopic(ProductTopicRq topic) throws Exception{
        if (StringUtils.isBlank(topic.getOperation())){
            throw new IllegalAccessException("设备对该Topic类的操作权限不能为空");
        }
        if (StringUtils.isBlank(topic.getProductKey())){
            throw new IllegalAccessException("Topic类的productKey不能为空");
        }
        if (StringUtils.isBlank(topic.getTopicShortName())){
            throw new IllegalAccessException("设置Topic类的自定义类目名称不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateProductTopicRequest request=new CreateProductTopicRequest();
        request.setProductKey(topic.getProductKey());
        request.setDesc(topic.getDesc());
        request.setOperation(topic.getOperation());
        request.setTopicShortName(topic.getTopicShortName());
        request.setIotInstanceId(topic.getIotInstanceId());
        CreateProductTopicResponse response = client.createProductTopic(request);
        System.out.println("产品创建自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
    //UpdateProductTopic
    public UpdateProductTopicResponse updateProductTopic(ProductTopicRq topic) throws Exception {
        if (StringUtils.isBlank(topic.getOperation())){
            throw new IllegalAccessException("设备对该Topic类的操作权限不能为空");
        }
        if (StringUtils.isBlank(topic.getTopicId())){
            throw new IllegalAccessException("修改的Topic类的ID不能为空");
        }
        if (StringUtils.isBlank(topic.getTopicShortName())){
            throw new IllegalAccessException("设置Topic类的自定义类目名称不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateProductTopicRequest request=new UpdateProductTopicRequest();
        request.setDesc(topic.getDesc());
        request.setIotInstanceId(topic.getIotInstanceId());
        request.setOperation(topic.getOperation());
        request.setTopicShortName(topic.getTopicShortName());
        request.setTopicId(topic.getTopicId());
        UpdateProductTopicResponse response = client.updateProductTopic(request);
        System.out.println("修改产品自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
    //QueryProductTopic
    public QueryProductTopicResponse queryProductTopic(ProductTopicRq topic) throws Exception {
        if (StringUtils.isBlank(topic.getProductKey())){
            throw new IllegalAccessException("Topic类的productKey不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryProductTopicRequest request=new QueryProductTopicRequest();
        request.setIotInstanceId(topic.getIotInstanceId());
        request.setProductKey(topic.getProductKey());
        QueryProductTopicResponse response = client.queryProductTopic(request);
        System.out.println("查询指定产品的自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteProductTopic
    public DeleteProductTopicResponse deleteProductTopic(ProductTopicRq topic) throws Exception {
        if (StringUtils.isBlank(topic.getTopicId())){
            throw new IllegalAccessException("修改的Topic类的ID不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteProductTopicRequest request=new DeleteProductTopicRequest();
        request.setIotInstanceId(topic.getIotInstanceId());
        request.setTopicId(topic.getTopicId());
        DeleteProductTopicResponse response = client.deleteProductTopic(request);
        System.out.println("删除产品自定义Topic类"+ JSON.toJSONString(response));
        return response;
    }
}
