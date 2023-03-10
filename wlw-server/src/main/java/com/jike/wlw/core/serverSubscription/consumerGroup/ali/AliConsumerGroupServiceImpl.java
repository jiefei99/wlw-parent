package com.jike.wlw.core.serverSubscription.consumerGroup.ali;

import com.aliyun.iot20180120.models.CreateConsumerGroupResponse;
import com.aliyun.iot20180120.models.DeleteConsumerGroupResponse;
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
import com.jike.wlw.common.DateUtils;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.serverSubscription.consumerGroup.ali.iot.ConsumerGroupManager;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupDeleteRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ali.AliConsumerGroupService;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ClientConnectionStatusVO;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ConsumerGroupVO;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ConsumerGroupStatusVO;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping(value = "service/aliConsumerGroup", produces = "application/json;charset=utf-8")
public class AliConsumerGroupServiceImpl extends BaseService implements AliConsumerGroupService {

    @Autowired
    private ConsumerGroupManager consumerGroup;

    @Override
    public String create(String tenantId, ConsumerGroupCreateRq createRq, String operator) {
        try {
            if (StringUtils.isBlank(createRq.getName())) {
                throw new IllegalAccessException("新建消费组名称不能为空");
            }
            CreateConsumerGroupResponse response = consumerGroup.createConsumerGroup(createRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("创建消费组失败：" + response.getBody().getErrorMessage());
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
            if (StringUtils.isBlank(modifyRq.getId())) {
                throw new IllegalAccessException("消费组ID不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getName())) {
                throw new IllegalAccessException("新消费组名称不能为空");
            }
            UpdateConsumerGroupResponse response = consumerGroup.updateConsumerGroup(modifyRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("修改消费组失败：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ConsumerGroup get(String tenantId, String groupId, String iotInstanceId) {
        try {
            if (StringUtils.isBlank(groupId)) {
                throw new IllegalAccessException("消费组ID不能为空");
            }
            QueryConsumerGroupByGroupIdResponse response = consumerGroup.queryConsumerGroupByGroupId(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("获取消费组失败：" + response.getBody().getErrorMessage());
            }
            QueryConsumerGroupByGroupIdResponseBodyData source = response.getBody().getData();
            ConsumerGroup target = new ConsumerGroup();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<ConsumerGroupVO> query(String tenantId, ConsumerGroupFilter filter) {
        try {
            QueryConsumerGroupListResponse response = consumerGroup.queryConsumerGroupList(filter);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("获取消费组列表失败：" + response.getBody().getErrorMessage());
            }
            List<ConsumerGroupVO> consumerGroupList = new ArrayList<>();
            if (response.getBody().getData() == null || response.getBody().getData().getConsumerGroupDTO() ==null || CollectionUtils.isEmpty(response.getBody().getData().getConsumerGroupDTO())) {
                return new PagingResult<>(filter.getPage(), filter.getPageSize(), response.getBody().getTotal(), consumerGroupList);
            }
            for (QueryConsumerGroupListResponseBodyDataConsumerGroupDTO info : response.getBody().getData().getConsumerGroupDTO()) {
                ConsumerGroupVO consumerGroupVO = new ConsumerGroupVO();
                consumerGroupVO.setId(info.getGroupId());
                consumerGroupVO.setName(info.getGroupName());
                consumerGroupVO.setCreated(DateUtils.dealDateFormat(info.getCreateTime(),"yyyy-MM-dd'T'HH:mm:ss.SSS Z"));
                consumerGroupList.add(consumerGroupVO);
            }
            return new PagingResult<>(filter.getPage(), filter.getPageSize(), response.getBody().getTotal(), consumerGroupList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ConsumerGroupStatusVO getStatus(String tenantId, String groupId, String iotInstanceId) {
        try {
            if (StringUtils.isBlank(groupId)) {
                throw new IllegalAccessException("消费组ID不能为空");
            }
            QueryConsumerGroupStatusResponse response = consumerGroup.queryConsumerGroupStatus(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("获取消费组状态失败：" + response.getBody().getErrorMessage());
            }
            ConsumerGroupStatusVO groupStatusVO=new ConsumerGroupStatusVO();
            if (response.getBody() == null ){
                return groupStatusVO;
            }
            BeanUtils.copyProperties(response.getBody(),groupStatusVO);
            if (StringUtils.isNotBlank(response.getBody().getLastConsumerTime())){
                groupStatusVO.setLastConsumerDateTime(DateUtils.dealDateFormat(response.getBody().getLastConsumerTime(),"yyyy-MM-dd'T'HH:mm:ss.SSS Z"));
            }
            if (response.getBody().getClientConnectionStatusList()==null||
            CollectionUtils.isEmpty( response.getBody().getClientConnectionStatusList().getConsumerGroupClientConnectionInfo())){
                return groupStatusVO;
            }
            List<ClientConnectionStatusVO> clientConnectionStatusVOList=new ArrayList<>();
            for (QueryConsumerGroupStatusResponseBodyClientConnectionStatusListConsumerGroupClientConnectionInfo info : response.getBody().getClientConnectionStatusList().getConsumerGroupClientConnectionInfo()) {
                ClientConnectionStatusVO clientConnectionStatusVO=new ClientConnectionStatusVO();
                clientConnectionStatusVO.setClientId(info.getClientId());
                clientConnectionStatusVO.setClientIpPort(info.getClientIpPort());
                clientConnectionStatusVO.setAccumulatedConsumeCountPerMinute(info.getAccumulatedConsumeCountPerMinute());
                clientConnectionStatusVO.setOnlineTime(new Date(info.getOnlineTime()));
                clientConnectionStatusVO.setRealTimeConsumeCountPerMinute(info.getRealTimeConsumeCountPerMinute());
                clientConnectionStatusVOList.add(clientConnectionStatusVO);
            }
            groupStatusVO.setClientStatusList(clientConnectionStatusVOList);
            return groupStatusVO;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void resetPosition(String tenantId, String groupId, String iotInstanceId) {
        try {
            if (StringUtils.isBlank(groupId)) {
                throw new IllegalAccessException("消费组ID不能为空");
            }
            ResetConsumerGroupPositionResponse response = consumerGroup.resetConsumerGroupPosition(groupId, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("消除消费组堆积消息：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, ConsumerGroupDeleteRq deleteRq, String operator) {
        try {
            if (StringUtils.isBlank(deleteRq.getId())) {
                throw new IllegalAccessException("消费组ID不能为空");
            }
            DeleteConsumerGroupResponse response = consumerGroup.deleteConsumerGroup(deleteRq.getId(), deleteRq.getIotInstanceId());
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("删除消费组失败：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}


