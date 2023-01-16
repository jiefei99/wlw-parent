package com.jike.wlw.core.product.serverSubscription.consumerGroup.ali;

import com.aliyun.iot20180120.models.CreateConsumerGroupResponse;
import com.aliyun.iot20180120.models.CreateConsumerGroupSubscribeRelationResponse;
import com.aliyun.iot20180120.models.DeleteConsumerGroupResponse;
import com.aliyun.iot20180120.models.DeleteConsumerGroupSubscribeRelationResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupByGroupIdResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupByGroupIdResponseBody.QueryConsumerGroupByGroupIdResponseBodyData;
import com.aliyun.iot20180120.models.QueryConsumerGroupListResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupListResponseBody.QueryConsumerGroupListResponseBodyDataConsumerGroupDTO;
import com.aliyun.iot20180120.models.QueryConsumerGroupStatusResponse;
import com.aliyun.iot20180120.models.QueryConsumerGroupStatusResponseBody.QueryConsumerGroupStatusResponseBodyClientConnectionStatusListConsumerGroupClientConnectionInfo;
import com.aliyun.iot20180120.models.ResetConsumerGroupPositionResponse;
import com.aliyun.iot20180120.models.UpdateConsumerGroupResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.serverSubscription.consumerGroup.ali.iot.ConsumerGroupManager;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ali.AliConsumerGroupService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: iot
 * @Author RS
 * @Date: 2023/1/13 15:26
 * @Version 1.0
 */

@Slf4j
@RestController("ConsumerGroupServiceAliImpl")
@ApiModel("阿里消费组实现")
public class AliConsumerGroupServiceImpl extends BaseService implements AliConsumerGroupService {

    @Autowired
    private ConsumerGroupManager consumerGroup;

    @Override
    public String create(String tenantId, ConsumerGroupCreateRq createRq, String operator) {
        try {
            CreateConsumerGroupResponse response = consumerGroup.createConsumerGroup(createRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("创建消费组失败："+response.getBody().getErrorMessage());
            }
            return response.getBody().getGroupId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, ConsumerGroupModifyRq modifyRq, String operator) {
        try {
            UpdateConsumerGroupResponse response = consumerGroup.updateConsumerGroup(modifyRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("修改消费组失败："+response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ConsumerGroup get(String tenantId, String groupId, String iotInstanceId) {
        try {
            QueryConsumerGroupByGroupIdResponse response = consumerGroup.queryConsumerGroupByGroupId(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("获取消费组失败："+response.getBody().getErrorMessage());
            }
            QueryConsumerGroupByGroupIdResponseBodyData source = response.getBody().getData();
            ConsumerGroup target=new ConsumerGroup();
            BeanUtils.copyProperties(source,target);
            return target;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<ConsumerGroup> query(String tenantId, ConsumerGroupFilter filter) {
        try {
            QueryConsumerGroupListResponse response = consumerGroup.queryConsumerGroupList(filter);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("获取消费组列表失败："+response.getBody().getErrorMessage());
            }
            List<ConsumerGroup> consumerGroupList =new ArrayList<>();
            if (response.getBody().getData()==null||CollectionUtils.isEmpty(response.getBody().getData().getConsumerGroupDTO())){
                return new PagingResult<>(filter.getCurrentPage(), filter.getPageSize(), response.getBody().getTotal(), consumerGroupList);
            }
            for (QueryConsumerGroupListResponseBodyDataConsumerGroupDTO info : response.getBody().getData().getConsumerGroupDTO()) {
                ConsumerGroup consumerGroup=new ConsumerGroup();
                consumerGroup.setGroupId(info.getGroupId());
                consumerGroup.setGroupName(info.getGroupName());
                consumerGroup.setCreateTime(info.getCreateTime());
                consumerGroupList.add(consumerGroup);
            }
            return new PagingResult<>(filter.getCurrentPage(), filter.getPageSize(), response.getBody().getTotal(), consumerGroupList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<ConsumerGroup> getStatus(String tenantId, String groupId, String iotInstanceId) {
        try {
            QueryConsumerGroupStatusResponse response = consumerGroup.queryConsumerGroupStatus(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("获取消费组状态失败："+response.getBody().getErrorMessage());
            }
            List<ConsumerGroup> consumerGroupList=new ArrayList<>();
            if (response.getBody().getClientConnectionStatusList()==null||CollectionUtils.isEmpty(response.getBody().getClientConnectionStatusList().getConsumerGroupClientConnectionInfo())){
                return consumerGroupList;
            }
            for (QueryConsumerGroupStatusResponseBodyClientConnectionStatusListConsumerGroupClientConnectionInfo info : response.getBody().getClientConnectionStatusList().getConsumerGroupClientConnectionInfo()) {
                ConsumerGroup consumerGroup=new ConsumerGroup();
                consumerGroup.setAccumulatedConsumeCountPerMinute(info.getAccumulatedConsumeCountPerMinute());
                consumerGroup.setClientId(info.getClientId());
                consumerGroup.setClientIpPort(info.getClientIpPort());
                consumerGroup.setOnlineTime(info.getOnlineTime());
                consumerGroup.setRealTimeConsumeCountPerMinute(info.getRealTimeConsumeCountPerMinute());
                consumerGroupList.add(consumerGroup);
            }
            return consumerGroupList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void resetPosition(String tenantId, String groupId, String iotInstanceId) {
        try {
            ResetConsumerGroupPositionResponse response = consumerGroup.resetConsumerGroupPosition(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("消除消费组堆积消息："+response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String groupId, String iotInstanceId) {
        try {
            DeleteConsumerGroupResponse response = consumerGroup.deleteConsumerGroup(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("删除消费组失败："+response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}


