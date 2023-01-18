package com.jike.wlw.service.serverSubscription.consumerGroup;

import com.geeker123.rumba.commons.paging.PagingResult;

import java.util.List;

public interface BaseConsumerGroupService {

    //CreateConsumerGroup
    String create(String tenantId, ConsumerGroupCreateRq createRq, String operator);

    //UpdateConsumerGroup
    void modify(String tenantId, ConsumerGroupModifyRq modifyRq, String operator);

    //QueryConsumerGroupByGroupId
    ConsumerGroup get(String tenantId, String groupId,String iotInstanceId);

    //QueryConsumerGroupList
    PagingResult<ConsumerGroup> query(String tenantId, ConsumerGroupFilter filter);

    //QueryConsumerGroupStatus
    List<ConsumerGroup> getStatus(String tenantId, String groupId,String iotInstanceId);

    //ResetConsumerGroupPosition
    void resetPosition(String tenantId, String groupId, String iotInstanceId);

    //DeleteConsumerGroup
    void delete(String tenantId, String groupId, String iotInstanceId);
}
