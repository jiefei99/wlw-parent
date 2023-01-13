/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月06日 16:14 - ASUS - 创建。
 */
package com.jike.wlw.core.product.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.core.product.ali.iot.entity.ConsumerGroupRq;
import com.jike.wlw.core.product.ali.iot.entity.SubscribeRelationRq;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author rs
 * @since 1.0
 */
@Slf4j
@Service
public class ServerSubscription {

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

    public static void main(String[] args) {
//        ServerSubscritionRq subscritionRq=new ServerSubscritionRq();
//        subscritionRq.setOtaEventFlag(true);
//        subscritionRq.setProductKey("a1GgN502dxa");
//        subscritionRq.setType("AMQP");
//        List<String> consumerGroupIds= Arrays.asList("LcJqlUzXLRb9yroihtWC000100");
//        subscritionRq.setConsumerGroupIdList(consumerGroupIds);
//        subscritionRq.setGroupName("新rs好帅啊2");
//        subscritionRq.setGroupId("LcJqlUzXLRb9yroihtWC000100");
//        subscritionRq.setCurrentPage("1");
//        subscritionRq.setPageSize("2");
//        subscritionRq.setFuzzy(true);
//        subscritionRq.setGroupName("帅");
        ServerSubscription serverSubscription=new ServerSubscription();
        try {
//            serverSubscription.deleteSubscribeRelation(subscritionRq);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //CreateSubscribeRelation
    public CreateSubscribeRelationResponse createSubscribeRelation(SubscribeRelationRq subscritionRq) throws Exception {
        if (StringUtils.isBlank(subscritionRq.getProductKey())){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        if ("MNS".equals(subscritionRq.getType())&&StringUtils.isBlank(subscritionRq.getMnsConfiguration())){
            throw new IllegalAccessException("MNS队列的配置信息不能为空");
        }
        if ("AMQP".equals(subscritionRq.getType())&& CollectionUtils.isEmpty(subscritionRq.getConsumerGroupIdList())){
            throw new IllegalAccessException("创建的AMQP订阅中的消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = client("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateSubscribeRelationRequest request =new CreateSubscribeRelationRequest();
        request.setProductKey(subscritionRq.getProductKey());
        request.setIotInstanceId(subscritionRq.getIotInstanceId());
        request.setDeviceDataFlag(subscritionRq.isDeviceDataFlag());
        request.setDeviceStatusChangeFlag(subscritionRq.isDeviceStatusChangeFlag());
        request.setDeviceLifeCycleFlag(subscritionRq.isDeviceLifeCycleFlag());
        request.setFoundDeviceListFlag(subscritionRq.isFoundDeviceListFlag());
        request.setThingHistoryFlag(subscritionRq.isThingHistoryFlag());
        request.setDeviceTopoLifeCycleFlag(subscritionRq.isDeviceTopoLifeCycleFlag());
        request.setOtaEventFlag(subscritionRq.isOtaEventFlag());
        request.setDeviceTagFlag(subscritionRq.isDeviceTagFlag());
        request.setOtaVersionFlag(subscritionRq.isOtaVersionFlag());
        request.setOtaJobFlag(subscritionRq.isOtaJobFlag());
        request.setType(subscritionRq.getType());
        request.setConsumerGroupIds(subscritionRq.getConsumerGroupIdList());
        request.setMnsConfiguration(subscritionRq.getMnsConfiguration());
        CreateSubscribeRelationResponse response = this.client.createSubscribeRelation(request);
        System.out.println("创建MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //UpdateSubscribeRelation
    public UpdateSubscribeRelationResponse updateSubscribeRelation(SubscribeRelationRq subscritionRq) throws Exception {
        if (StringUtils.isBlank(subscritionRq.getType())){
            throw new IllegalAccessException("选择查询的订阅类型不能为空");
        }
        if (StringUtils.isBlank(subscritionRq.getProductKey())){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        if ("MNS".equals(subscritionRq.getType())&&StringUtils.isBlank(subscritionRq.getMnsConfiguration())){
            throw new IllegalAccessException("MNS队列的配置信息不能为空");
        }
        if ("AMQP".equals(subscritionRq.getType())&& CollectionUtils.isEmpty(subscritionRq.getConsumerGroupIdList())){
            throw new IllegalAccessException("创建的AMQP订阅中的消费组ID不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateSubscribeRelationRequest request =new UpdateSubscribeRelationRequest();
        request.setProductKey(subscritionRq.getProductKey());
        request.setType(subscritionRq.getType());
        request.setIotInstanceId(subscritionRq.getIotInstanceId());
        request.setDeviceDataFlag(subscritionRq.isDeviceDataFlag());
        request.setDeviceStatusChangeFlag(subscritionRq.isDeviceStatusChangeFlag());
        request.setFoundDeviceListFlag(subscritionRq.isFoundDeviceListFlag());
        request.setDeviceLifeCycleFlag(subscritionRq.isDeviceLifeCycleFlag());
        request.setDeviceTopoLifeCycleFlag(subscritionRq.isDeviceTopoLifeCycleFlag());
        request.setThingHistoryFlag(subscritionRq.isThingHistoryFlag());
        request.setOtaEventFlag(subscritionRq.isOtaEventFlag());
        request.setDeviceTagFlag(subscritionRq.isDeviceTagFlag());
        request.setOtaVersionFlag(subscritionRq.isOtaVersionFlag());
        request.setOtaJobFlag(subscritionRq.isOtaJobFlag());
        request.setMnsConfiguration(subscritionRq.getMnsConfiguration());
        request.setConsumerGroupIds(subscritionRq.getConsumerGroupIdList());
        UpdateSubscribeRelationResponse response = this.client.updateSubscribeRelation(request);
        System.out.println("修改MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //QuerySubscribeRelation
    public QuerySubscribeRelationResponse querySubscribeRelation(SubscribeRelationRq subscritionRq) throws Exception {
        if (StringUtils.isBlank(subscritionRq.getType())){
            throw new IllegalAccessException("选择查询的订阅类型不能为空");
        }
        if (StringUtils.isBlank(subscritionRq.getProductKey())){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QuerySubscribeRelationRequest request =new QuerySubscribeRelationRequest();
        request.setProductKey(subscritionRq.getProductKey());
        request.setIotInstanceId(subscritionRq.getIotInstanceId());
        request.setType(subscritionRq.getType());
        QuerySubscribeRelationResponse response = this.client.querySubscribeRelation(request);
        System.out.println("查询MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteSubscribeRelation
    public DeleteSubscribeRelationResponse deleteSubscribeRelation(SubscribeRelationRq subscritionRq) throws Exception {
        if (StringUtils.isBlank(subscritionRq.getType())){
            throw new IllegalAccessException("选择删除的订阅类型不能为空");
        }
        if (StringUtils.isBlank(subscritionRq.getProductKey())){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteSubscribeRelationRequest request =new DeleteSubscribeRelationRequest();
        request.setProductKey(subscritionRq.getProductKey());
        request.setIotInstanceId(subscritionRq.getIotInstanceId());
        request.setType(subscritionRq.getType());
        DeleteSubscribeRelationResponse response = this.client.deleteSubscribeRelation(request);
        System.out.println("删除MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //CreateConsumerGroup
    public CreateConsumerGroupResponse createConsumerGroup(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getGroupName())){
            throw new IllegalAccessException("新建消费组名称不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateConsumerGroupRequest request =new CreateConsumerGroupRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setGroupName(consumerGroupRq.getGroupName());
        CreateConsumerGroupResponse response = this.client.createConsumerGroup(request);
        System.out.println("创建一个消费组"+ JSON.toJSONString(response));
        return response;
    }
    //UpdateConsumerGroup
    public UpdateConsumerGroupResponse updateConsumerGroup(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("消费组ID不能为空");
        }
        if (StringUtils.isBlank(consumerGroupRq.getGroupName())){
            throw new IllegalAccessException("新消费组名称不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateConsumerGroupRequest request =new UpdateConsumerGroupRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setGroupId(consumerGroupRq.getGroupId());
        request.setNewGroupName(consumerGroupRq.getGroupName());
        UpdateConsumerGroupResponse response = this.client.updateConsumerGroup(request);
        System.out.println("修改消费组名称"+ JSON.toJSONString(response));
        return response;
    }
    //QueryConsumerGroupByGroupId
    public QueryConsumerGroupByGroupIdResponse queryConsumerGroupByGroupId(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryConsumerGroupByGroupIdRequest request =new QueryConsumerGroupByGroupIdRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setGroupId(consumerGroupRq.getGroupId());
        QueryConsumerGroupByGroupIdResponse response = this.client.queryConsumerGroupByGroupId(request);
        System.out.println("根据消费组ID查询消费组详情"+ JSON.toJSONString(response));
        return response;
    }
    //QueryConsumerGroupList
    public QueryConsumerGroupListResponse queryConsumerGroupList(ConsumerGroupRq consumerGroupRq) throws Exception {
       if (consumerGroupRq.getCurrentPage()==null||consumerGroupRq.getCurrentPage()<1){
           throw new IllegalAccessException("指定显示返回结果中页数不能为空且最小值为1");
       }
       if (consumerGroupRq.getPageSize()==null||consumerGroupRq.getPageSize()<1||consumerGroupRq.getPageSize()>1000){
           throw new IllegalAccessException("指定显示返回结果中消费组数量不能为空且最小值为1最大值为1000");
       }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryConsumerGroupListRequest request =new QueryConsumerGroupListRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setCurrentPage(consumerGroupRq.getCurrentPage());
        request.setFuzzy(consumerGroupRq.isFuzzy());
        if (consumerGroupRq.isFuzzy()){
            request.setGroupName(consumerGroupRq.getGroupName());
        }
        request.setPageSize(consumerGroupRq.getPageSize());
        QueryConsumerGroupListResponse response = this.client.queryConsumerGroupList(request);
        System.out.println("查询用户所有消费组列表"+ JSON.toJSONString(response));
        return response;
    }
    //QueryConsumerGroupStatus
    public QueryConsumerGroupStatusResponse queryConsumerGroupStatus(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryConsumerGroupStatusRequest request =new QueryConsumerGroupStatusRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setGroupId(consumerGroupRq.getGroupId());
        QueryConsumerGroupStatusResponse response = this.client.queryConsumerGroupStatus(request);
        System.out.println("使用AMQP服务端订阅时，查询某个消费组的状态，包括在线客户端信息、消息消费速率、消息堆积数、最近消息消费时间"+ JSON.toJSONString(response));
        return response;
    }
    //ResetConsumerGroupPosition
    public ResetConsumerGroupPositionResponse resetConsumerGroupPosition(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ResetConsumerGroupPositionRequest request =new ResetConsumerGroupPositionRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setGroupId(consumerGroupRq.getGroupId());
        ResetConsumerGroupPositionResponse response = this.client.resetConsumerGroupPosition(request);
        System.out.println("使用AMQP服务端订阅时，清空某个消费组的堆积消息"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteConsumerGroup
    public DeleteConsumerGroupResponse deleteConsumerGroup(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("消费组ID不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteConsumerGroupRequest request =new DeleteConsumerGroupRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setGroupId(consumerGroupRq.getGroupId());
        DeleteConsumerGroupResponse response = this.client.deleteConsumerGroup(request);
        System.out.println("删除消费组。"+ JSON.toJSONString(response));
        return response;
    }
    //CreateConsumerGroupSubscribeRelation
    public CreateConsumerGroupSubscribeRelationResponse createConsumerGroupSubscribeRelation(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getProductKey())){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("订阅中的消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateConsumerGroupSubscribeRelationRequest request =new CreateConsumerGroupSubscribeRelationRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setConsumerGroupId(consumerGroupRq.getGroupId());
        request.setProductKey(consumerGroupRq.getProductKey());
        CreateConsumerGroupSubscribeRelationResponse response = this.client.createConsumerGroupSubscribeRelation(request);
        System.out.println("在AMQP订阅中添加一个消费组。"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteConsumerGroupSubscribeRelation
    public DeleteConsumerGroupSubscribeRelationResponse deleteConsumerGroupSubscribeRelation(ConsumerGroupRq consumerGroupRq) throws Exception {
        if (StringUtils.isBlank(consumerGroupRq.getProductKey())){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        if (StringUtils.isBlank(consumerGroupRq.getGroupId())){
            throw new IllegalAccessException("订阅中的消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteConsumerGroupSubscribeRelationRequest request =new DeleteConsumerGroupSubscribeRelationRequest();
        request.setIotInstanceId(consumerGroupRq.getIotInstanceId());
        request.setConsumerGroupId(consumerGroupRq.getGroupId());
        request.setProductKey(consumerGroupRq.getProductKey());
        DeleteConsumerGroupSubscribeRelationResponse response = this.client.deleteConsumerGroupSubscribeRelation(request);
        System.out.println("从AMQP订阅中的多个消费组移除指定消费组。"+ JSON.toJSONString(response));
        return response;
    }
}
