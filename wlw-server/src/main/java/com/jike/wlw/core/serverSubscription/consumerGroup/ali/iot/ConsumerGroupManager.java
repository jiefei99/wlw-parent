package com.jike.wlw.core.serverSubscription.consumerGroup.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.models.CreateConsumerGroupRequest;
import com.aliyun.iot20180120.models.CreateConsumerGroupResponse;
import com.aliyun.iot20180120.models.DeleteConsumerGroupRequest;
import com.aliyun.iot20180120.models.DeleteConsumerGroupResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupByGroupIdRequest;
import com.aliyun.iot20180120.models.QueryConsumerGroupByGroupIdResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupListRequest;
import com.aliyun.iot20180120.models.QueryConsumerGroupListResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupStatusRequest;
import com.aliyun.iot20180120.models.QueryConsumerGroupStatusResponse;
import com.aliyun.iot20180120.models.ResetConsumerGroupPositionRequest;
import com.aliyun.iot20180120.models.ResetConsumerGroupPositionResponse;
import com.aliyun.iot20180120.models.UpdateConsumerGroupRequest;
import com.aliyun.iot20180120.models.UpdateConsumerGroupResponse;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: ConsumerGroupManager
 * @Author RS
 * @Date: 2023/1/13 15:36
 * @Version 1.0
 */

@Slf4j
@Component
public class ConsumerGroupManager {

    @Autowired
    private AliIotClient client;

    public static void main(String[] args) {

    }

    //CreateConsumerGroup
    public CreateConsumerGroupResponse createConsumerGroup(ConsumerGroupCreateRq createRq) throws Exception {
        if (StringUtils.isBlank(createRq.getName())){
            throw new IllegalAccessException("新建消费组名称不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateConsumerGroupRequest request =new CreateConsumerGroupRequest();
        request.setIotInstanceId(createRq.getIotInstanceId());
        request.setGroupName(createRq.getName());
        CreateConsumerGroupResponse response = client.createConsumerGroup(request);
        System.out.println("创建一个消费组"+ JSON.toJSONString(response));
        return response;
    }
    //UpdateConsumerGroup
    public UpdateConsumerGroupResponse updateConsumerGroup(ConsumerGroupModifyRq modifyRq) throws Exception {
        if (StringUtils.isBlank(modifyRq.getId())){
            throw new IllegalAccessException("消费组ID不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getName())){
            throw new IllegalAccessException("新消费组名称不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateConsumerGroupRequest request =new UpdateConsumerGroupRequest();
        request.setIotInstanceId(modifyRq.getIotInstanceId());
        request.setGroupId(modifyRq.getId());
        request.setNewGroupName(modifyRq.getName());
        UpdateConsumerGroupResponse response = client.updateConsumerGroup(request);
        System.out.println("修改消费组名称"+ JSON.toJSONString(response));
        return response;
    }
    //QueryConsumerGroupByGroupId
    public QueryConsumerGroupByGroupIdResponse queryConsumerGroupByGroupId(String groupId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(groupId)){
            throw new IllegalAccessException("消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryConsumerGroupByGroupIdRequest request =new QueryConsumerGroupByGroupIdRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setGroupId(groupId);
        QueryConsumerGroupByGroupIdResponse response = client.queryConsumerGroupByGroupId(request);
        System.out.println("根据消费组ID查询消费组详情"+ JSON.toJSONString(response));
        return response;
    }
    //QueryConsumerGroupList
    public QueryConsumerGroupListResponse queryConsumerGroupList(ConsumerGroupFilter filter) throws Exception {
        if (filter.getPage()<1){
            throw new IllegalAccessException("指定显示返回结果中页数不能为空且最小值为1");
        }
        if (filter.getPageSize()<1||filter.getPageSize()>1000){
            throw new IllegalAccessException("指定显示返回结果中消费组数量不能为空且最小值为1最大值为1000");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryConsumerGroupListRequest request =new QueryConsumerGroupListRequest();
        request.setIotInstanceId(filter.getIotInstanceId());
        request.setCurrentPage(filter.getPage());
        request.setFuzzy(filter.isFuzzy());
        if (filter.isFuzzy()){
            request.setGroupName(filter.getNameLike());
        }
        request.setPageSize(filter.getPageSize());
        QueryConsumerGroupListResponse response = client.queryConsumerGroupList(request);
        System.out.println("查询用户所有消费组列表"+ JSON.toJSONString(response));
        return response;
    }
    //QueryConsumerGroupStatus
    public QueryConsumerGroupStatusResponse queryConsumerGroupStatus(String groupId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(groupId)){
            throw new IllegalAccessException("消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryConsumerGroupStatusRequest request =new QueryConsumerGroupStatusRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setGroupId(groupId);
        QueryConsumerGroupStatusResponse response = client.queryConsumerGroupStatus(request);
        System.out.println("使用AMQP服务端订阅时，查询某个消费组的状态，包括在线客户端信息、消息消费速率、消息堆积数、最近消息消费时间"+ JSON.toJSONString(response));
        return response;
    }
    //ResetConsumerGroupPosition
    public ResetConsumerGroupPositionResponse resetConsumerGroupPosition(String groupId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(groupId)){
            throw new IllegalAccessException("消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        ResetConsumerGroupPositionRequest request =new ResetConsumerGroupPositionRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setGroupId(groupId);
        ResetConsumerGroupPositionResponse response = client.resetConsumerGroupPosition(request);
        System.out.println("使用AMQP服务端订阅时，清空某个消费组的堆积消息"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteConsumerGroup
    public DeleteConsumerGroupResponse deleteConsumerGroup(String groupId, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(groupId)){
            throw new IllegalAccessException("消费组ID不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteConsumerGroupRequest request =new DeleteConsumerGroupRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setGroupId(groupId);
        DeleteConsumerGroupResponse response = client.deleteConsumerGroup(request);
        System.out.println("删除消费组。"+ JSON.toJSONString(response));
        return response;
    }
}


