package com.jike.wlw.core.serverSubscription.subscribe.ali;

import com.aliyun.iot20180120.models.CreateConsumerGroupSubscribeRelationResponse;
import com.aliyun.iot20180120.models.CreateSubscribeRelationResponse;
import com.aliyun.iot20180120.models.DeleteConsumerGroupSubscribeRelationResponse;
import com.aliyun.iot20180120.models.DeleteSubscribeRelationResponse;
import com.aliyun.iot20180120.models.QuerySubscribeRelationResponse;
import com.aliyun.iot20180120.models.UpdateSubscribeRelationResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.serverSubscription.subscribe.ali.iot.SubscribeRelationManager;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationModifyRq;
import com.jike.wlw.service.serverSubscription.subscribe.ali.AliSubscribeRelationService;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: SubscribeRelationServiceImpl
 * @Author RS
 * @Date: 2023/1/14 9:57
 * @Version 1.0
 */
@Slf4j
@RestController("subscribeRelationServiceAliImpl")
@ApiModel("阿里订阅实现")
@RequestMapping(value = "service/subscribeRelation", produces = "application/json;charset=utf-8")
public class AliSubscribeRelationServiceImpl extends BaseService implements AliSubscribeRelationService {
    @Autowired
    private SubscribeRelationManager subscribeRelationManager;

    @Override
    public String create(String tenantId, SubscribeRelationCreateRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(createRq.getProductKey())) {
                throw new BusinessException("订阅中的产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(createRq.getType())) {
                throw new BusinessException("订阅类型不能为空！");
            }
            CreateSubscribeRelationResponse response = subscribeRelationManager.createSubscribeRelation(createRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("创建订阅失败，原因：" + response.getBody().getErrorMessage());
            }
            return response.getBody().getRequestId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, SubscribeRelationModifyRq modifyRq, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(modifyRq.getType())) {
                throw new BusinessException("选择查询的订阅类型不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getProductKey())) {
                throw new BusinessException("订阅中的产品的ProductKey不能为空");
            }
            UpdateSubscribeRelationResponse response = subscribeRelationManager.updateSubscribeRelation(modifyRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("修改订阅失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String productKey, String type, String iotInstanceId) throws BusinessException {
        try {
            if (StringUtils.isBlank(type)) {
                throw new BusinessException("选择删除的订阅类型不能为空");
            }
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("订阅中的产品的ProductKey不能为空");
            }
            DeleteSubscribeRelationResponse response = subscribeRelationManager.deleteSubscribeRelation(productKey, type, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("删除订阅失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public SubscribeRelation get(String tenantId, String productKey, String type, String iotInstanceId) throws BusinessException {
        try {
            if (StringUtils.isBlank(type)) {
                throw new BusinessException("选择查询的订阅类型不能为空");
            }
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("订阅中的产品的ProductKey不能为空");
            }
            QuerySubscribeRelationResponse response = subscribeRelationManager.querySubscribeRelation(productKey, type, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("查询订阅失败，原因：" + response.getBody().getErrorMessage());
            }
            SubscribeRelation subscribeRelation = new SubscribeRelation();
            subscribeRelation.setConsumerGroupIds(response.getBody().getConsumerGroupIds());
            List<String> pushMsgTypeList = new ArrayList<>();
            addListProp(pushMsgTypeList, response.getBody().getDeviceDataFlag(), SubscribeRelation.DEVICE_DATA_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getDeviceLifeCycleFlag(), SubscribeRelation.DEVICE_LIFE_CYCLE_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getDeviceStatusChangeFlag(), SubscribeRelation.DEVICE_STATUS_CHANGE_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getDeviceTagFlag(), SubscribeRelation.DEVICE_TAG_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getDeviceTopoLifeCycleFlag(), SubscribeRelation.DEVICE_TOPO_LIFE_CYCLE_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getFoundDeviceListFlag(), SubscribeRelation.FOUND_DEVICE_LIST_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getOtaEventFlag(), SubscribeRelation.OTA_EVENT_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getOtaJobFlag(), SubscribeRelation.OTA_JOB_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getOtaVersionFlag(), SubscribeRelation.OTA_VERSION_FLAG);
            addListProp(pushMsgTypeList, response.getBody().getThingHistoryFlag(), SubscribeRelation.THING_HISTORY_FLAG);
            subscribeRelation.setProductKey(response.getBody().getProductKey());
            subscribeRelation.setMnsConfiguration(response.getBody().getMnsConfiguration());
            subscribeRelation.setPushMessageType(pushMsgTypeList);
            subscribeRelation.setType(response.getBody().getType());
            return subscribeRelation;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }


    @Override
    public String addSubscribeRelation(String tenantId, ConsumerGroupSubscribeCreateRq createRq, String operator) {
        try {
            if (StringUtils.isBlank(createRq.getProductKey())) {
                throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(createRq.getGroupId())) {
                throw new IllegalAccessException("订阅中的消费组ID不能为空");
            }
            CreateConsumerGroupSubscribeRelationResponse response = subscribeRelationManager.createConsumerGroupSubscribeRelation(createRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("订阅中添加消费组失败：" + response.getBody().getErrorMessage());
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteSubscribeRelation(String tenantId, String groupId, String productKey, String operator, String iotInstanceId) {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new IllegalAccessException("订阅中的产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(groupId)) {
                throw new IllegalAccessException("订阅中的消费组ID不能为空");
            }
            DeleteConsumerGroupSubscribeRelationResponse response = subscribeRelationManager.deleteConsumerGroupSubscribeRelation(groupId, productKey, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("移除订阅中的指定消费组失败：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    // list需要添加数据的List flag判断是否添加 data添加的数据
    private void addListProp(List<String> list, boolean flag, String data) {
        if (flag) {
            list.add(data);
        }
    }
}


