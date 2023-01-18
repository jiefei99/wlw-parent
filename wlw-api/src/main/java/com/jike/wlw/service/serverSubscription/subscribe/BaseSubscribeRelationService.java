package com.jike.wlw.service.serverSubscription.subscribe;

import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;

public interface BaseSubscribeRelationService {
    String create(String tenantId, SubscribeRelationCreateRq createRq, String operator);

    void modify(String tenantId, SubscribeRelationModifyRq modifyRq, String operator);

    SubscribeRelation get(String tenantId, String productKey, String type, String iotInstanceId);

    void delete(String tenantId, String productKey, String type, String iotInstanceId);

    //CreateConsumerGroupSubscribeRelation
    String addSubscribeRelation(String tenantId, ConsumerGroupSubscribeCreateRq createRq, String operator);

    //DeleteConsumerGroupSubscribeRelation
    void deleteSubscribeRelation(String tenantId, String groupId, String productKey, String iotInstanceId);

}
