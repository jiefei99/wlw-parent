package com.jike.wlw.core.serverSubscription.subscribe.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.models.CreateConsumerGroupSubscribeRelationRequest;
import com.aliyun.iot20180120.models.CreateConsumerGroupSubscribeRelationResponse;
import com.aliyun.iot20180120.models.CreateSubscribeRelationRequest;
import com.aliyun.iot20180120.models.CreateSubscribeRelationResponse;
import com.aliyun.iot20180120.models.DeleteConsumerGroupSubscribeRelationRequest;
import com.aliyun.iot20180120.models.DeleteConsumerGroupSubscribeRelationResponse;
import com.aliyun.iot20180120.models.DeleteSubscribeRelationRequest;
import com.aliyun.iot20180120.models.DeleteSubscribeRelationResponse;
import com.aliyun.iot20180120.models.QuerySubscribeRelationRequest;
import com.aliyun.iot20180120.models.QuerySubscribeRelationResponse;
import com.aliyun.iot20180120.models.UpdateSubscribeRelationRequest;
import com.aliyun.iot20180120.models.UpdateSubscribeRelationResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationModifyRq;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: SubscribeRelationManager
 * @Author RS
 * @Date: 2023/1/14 10:06
 * @Version 1.0
 */

@Slf4j
@Component
public class SubscribeRelationManager {

    @Autowired
    private AliIotClient client;

    //CreateSubscribeRelation
    public CreateSubscribeRelationResponse createSubscribeRelation(SubscribeRelationCreateRq subscritionRq) throws Exception {
        if (StringUtils.isBlank(subscritionRq.getProductKey())){
            throw new BusinessException("订阅中的产品的ProductKey不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(subscritionRq.getType())){
            throw new BusinessException("订阅类型不能为空！");
        }
        if ("MNS".equals(subscritionRq.getType())&&StringUtils.isBlank(subscritionRq.getMnsConfiguration())){
            throw new BusinessException("MNS队列的配置信息不能为空");
        }
        //consumerGroupIds
        if ("AMQP".equals(subscritionRq.getType())&& CollectionUtils.isEmpty(subscritionRq.getConsumerGroupIds())){
            throw new BusinessException("创建的AMQP订阅中的消费组ID不能为空");
        }
//        Client client = client("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateSubscribeRelationRequest request =new CreateSubscribeRelationRequest();
        request.setProductKey(subscritionRq.getProductKey());
        request.setIotInstanceId(subscritionRq.getIotInstanceId());
        request.setDeviceDataFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_DATA_FLAG));
        request.setDeviceStatusChangeFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_STATUS_CHANGE_FLAG));
        request.setDeviceLifeCycleFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_LIFE_CYCLE_FLAG));
        request.setFoundDeviceListFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.FOUND_DEVICE_LIST_FLAG));
        request.setThingHistoryFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.THING_HISTORY_FLAG));
        request.setDeviceTopoLifeCycleFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_TOPO_LIFE_CYCLE_FLAG));
        request.setOtaEventFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.OTA_EVENT_FLAG));
        request.setDeviceTagFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_TAG_FLAG));
        request.setOtaVersionFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.OTA_VERSION_FLAG));
        request.setOtaJobFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.OTA_JOB_FLAG));
        request.setType(subscritionRq.getType());
        request.setConsumerGroupIds(subscritionRq.getConsumerGroupIds());
        request.setMnsConfiguration(subscritionRq.getMnsConfiguration());
        CreateSubscribeRelationResponse response = client.createSubscribeRelation(request);
        System.out.println("创建MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //UpdateSubscribeRelation
    public UpdateSubscribeRelationResponse updateSubscribeRelation(SubscribeRelationModifyRq subscritionRq) throws Exception {
        if (StringUtils.isBlank(subscritionRq.getType())){
            throw new BusinessException("选择查询的订阅类型不能为空");
        }
        if (StringUtils.isBlank(subscritionRq.getProductKey())){
            throw new BusinessException("订阅中的产品的ProductKey不能为空");
        }
        if ("MNS".equals(subscritionRq.getType())&&StringUtils.isBlank(subscritionRq.getMnsConfiguration())){
            throw new BusinessException("MNS队列的配置信息不能为空");
        }
        if ("AMQP".equals(subscritionRq.getType())&& CollectionUtils.isEmpty(subscritionRq.getConsumerGroupIds())){
            throw new BusinessException("创建的AMQP订阅中的消费组ID不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateSubscribeRelationRequest request =new UpdateSubscribeRelationRequest();
        request.setProductKey(subscritionRq.getProductKey());
        request.setType(subscritionRq.getType());
        request.setIotInstanceId(subscritionRq.getIotInstanceId());
        request.setDeviceDataFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_DATA_FLAG));
        request.setDeviceStatusChangeFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_STATUS_CHANGE_FLAG));
        request.setDeviceLifeCycleFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_LIFE_CYCLE_FLAG));
        request.setFoundDeviceListFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.FOUND_DEVICE_LIST_FLAG));
        request.setThingHistoryFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.THING_HISTORY_FLAG));
        request.setDeviceTopoLifeCycleFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_TOPO_LIFE_CYCLE_FLAG));
        request.setOtaEventFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.OTA_EVENT_FLAG));
        request.setDeviceTagFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.DEVICE_TAG_FLAG));
        request.setOtaVersionFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.OTA_VERSION_FLAG));
        request.setOtaJobFlag(subscritionRq.getPushMessageType().contains(SubscribeRelation.OTA_JOB_FLAG));
        request.setMnsConfiguration(subscritionRq.getMnsConfiguration());
        request.setConsumerGroupIds(subscritionRq.getConsumerGroupIds());
        UpdateSubscribeRelationResponse response = client.updateSubscribeRelation(request);
        System.out.println("修改MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //QuerySubscribeRelation
    public QuerySubscribeRelationResponse querySubscribeRelation(String productKey, String type, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(type)){
            throw new BusinessException("选择查询的订阅类型不能为空");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("订阅中的产品的ProductKey不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QuerySubscribeRelationRequest request =new QuerySubscribeRelationRequest();
        request.setProductKey(productKey);
        request.setIotInstanceId(iotInstanceId);
        request.setType(type);
        QuerySubscribeRelationResponse response = client.querySubscribeRelation(request);
        System.out.println("查询MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }
    //DeleteSubscribeRelation
    public DeleteSubscribeRelationResponse deleteSubscribeRelation(String productKey, String type, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(type)){
            throw new BusinessException("选择删除的订阅类型不能为空");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("订阅中的产品的ProductKey不能为空");
        }
        //        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteSubscribeRelationRequest request =new DeleteSubscribeRelationRequest();
        request.setProductKey(productKey);
        request.setIotInstanceId(iotInstanceId);
        request.setType(type);
        DeleteSubscribeRelationResponse response = client.deleteSubscribeRelation(request);
        System.out.println("删除MNS或AMQP服务端订阅"+ JSON.toJSONString(response));
        return response;
    }

    //CreateConsumerGroupSubscribeRelation
    public CreateConsumerGroupSubscribeRelationResponse createConsumerGroupSubscribeRelation(ConsumerGroupSubscribeCreateRq consumerGroupRq) throws Exception {
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
        CreateConsumerGroupSubscribeRelationResponse response = client.createConsumerGroupSubscribeRelation(request);
        System.out.println("在AMQP订阅中添加一个消费组。"+ JSON.toJSONString(response));
        return response;
    }

    //DeleteConsumerGroupSubscribeRelation
    public DeleteConsumerGroupSubscribeRelationResponse deleteConsumerGroupSubscribeRelation(String groupId, String productKey, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)){
            throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
        }
        if (StringUtils.isBlank(groupId)){
            throw new IllegalAccessException("订阅中的消费组ID不能为空");
        }
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteConsumerGroupSubscribeRelationRequest request =new DeleteConsumerGroupSubscribeRelationRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setConsumerGroupId(groupId);
        request.setProductKey(productKey);
        DeleteConsumerGroupSubscribeRelationResponse response = client.deleteConsumerGroupSubscribeRelation(request);
        System.out.println("从AMQP订阅中的多个消费组移除指定消费组。"+ JSON.toJSONString(response));
        return response;
    }

}


