package com.jike.wlw.core.serverSubscription.subscribe.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.dao.product.info.ProductDao;
import com.jike.wlw.dao.serverSubscription.subscribe.PSubscribe;
import com.jike.wlw.dao.serverSubscription.subscribe.SubscribeDao;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeFilter;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationModifyRq;
import com.jike.wlw.service.serverSubscription.subscribe.privatization.PrivateSubscribeRelationService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @title: PrivateSubscribeRelationServiceImpl
 * @Author RS
 * @Date: 2023/1/14 15:07
 * @Version 1.0
 */
@Slf4j
@RestController("subscribeRelationServicePrivateImpl")
@ApiModel("私有化订阅实现")
public class PrivateSubscribeRelationServiceImpl extends BaseService implements PrivateSubscribeRelationService {
    @Autowired
    private SubscribeDao subscribeDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public String create(String tenantId, SubscribeRelationCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(createRq.getType())) {
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(createRq.getProductKey())) {
            throw new BusinessException("ProductKey不能为空！");
        }
        if ("MNS".equals(createRq.getType()) && StringUtils.isBlank(createRq.getMnsConfiguration())) {
            throw new BusinessException("MNS队列的配置信息不能为空");
        }
        //consumerGroupIds
        if ("AMQP".equals(createRq.getType()) && CollectionUtils.isEmpty(createRq.getConsumerGroupIds())) {
            throw new BusinessException("创建的AMQP订阅中的消费组ID不能为空");
        }
        if (CollectionUtils.isEmpty(createRq.getPushMessageType())) {
            throw new BusinessException("订阅消息不能为空");
        }
        try {
            PProduct perz = productDao.get(PProduct.class, "productKey", createRq.getProductKey(), "tenantId", tenantId, "isDeleted", 0);
            if (perz == null) {
                throw new BusinessException("产品不存在！");
            }
            List<PSubscribe> subscribeList = new ArrayList<>();
            for (String info : createRq.getPushMessageType()) {
                if ("AMQP".equals(createRq.getType())) {
                    for (String groupId : createRq.getConsumerGroupIds()) {
                        PSubscribe amqpSubscribe = new PSubscribe();
                        amqpSubscribe.setPushMessageType(info);
                        amqpSubscribe.setConsumerGroupId(groupId);
                        amqpSubscribe.setSubscribeFlags(createRq.getSubscribeFlags());
                        amqpSubscribe.onCreated(operator);
                        amqpSubscribe.setTenantId(tenantId);
                        amqpSubscribe.setType(createRq.getType());
                        amqpSubscribe.setProductKey(createRq.getProductKey());
                        subscribeList.add(amqpSubscribe);
                    }
                } else if ("MNS".equals(createRq.getType())) {
                    PSubscribe mnsSubscribe = new PSubscribe();
                    if (StringUtils.isNotBlank(createRq.getMnsConfiguration())) {
                        mnsSubscribe.setMnsConfiguration(createRq.getMnsConfiguration());
                    }
                }
            }
            subscribeDao.save(subscribeList);
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, SubscribeRelationModifyRq modifyRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(modifyRq.getType())) {
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(modifyRq.getProductKey())) {
            throw new BusinessException("ProductKey不能为空！");
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        try {
            subscribeDao.removeSubscribe(tenantId, modifyRq.getType(), modifyRq.getProductKey());
            List<PSubscribe> subscribeList = new ArrayList<>();
            for (String info : modifyRq.getPushMessageType()) {
                if ("AMQP".equals(modifyRq.getType())) {
                    for (String groupId : modifyRq.getConsumerGroupIds()) {
                        PSubscribe amqpSubscribe = new PSubscribe();
                        amqpSubscribe.setPushMessageType(info);
                        amqpSubscribe.setConsumerGroupId(groupId);
                        amqpSubscribe.setSubscribeFlags(modifyRq.getSubscribeFlags());
                        amqpSubscribe.onCreated(operator);
                        amqpSubscribe.setTenantId(tenantId);
                        amqpSubscribe.setType(modifyRq.getType());
                        amqpSubscribe.setProductKey(modifyRq.getProductKey());
                        subscribeList.add(amqpSubscribe);
                    }
                } else if ("MNS".equals(modifyRq.getType())) {
                    PSubscribe mnsSubscribe = new PSubscribe();
                    if (StringUtils.isNotBlank(modifyRq.getMnsConfiguration())) {
                        mnsSubscribe.setMnsConfiguration(modifyRq.getMnsConfiguration());
                    }
                }
            }
            subscribeDao.save(subscribeList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String productKey, String type, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(type)) {
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("ProductKey不能为空！");
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        try {
            subscribeDao.removeSubscribe(tenantId, type, productKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public SubscribeRelation get(String tenantId, String productKey, String type, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(type)) {
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("ProductKey不能为空！");
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        try {
            SubscribeFilter filter = new SubscribeFilter();
            filter.setType(type);
            filter.setProductKey(productKey);
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)) {
                return null;
            }
            Set<String> msgTypes = new HashSet<>();
            Set<String> amqpGroupIds = new HashSet<>();
            for (PSubscribe subscribe : subscribeList) {
                msgTypes.add(subscribe.getPushMessageType());
                if ("AMQP".equals(subscribe.getType())) {
                    amqpGroupIds.add(subscribe.getConsumerGroupId());
                }
            }
            SubscribeRelation subscribeRelation = new SubscribeRelation();
            subscribeRelation.setType(type);
            subscribeRelation.setConsumerGroupIds(new ArrayList<>(amqpGroupIds));
            subscribeRelation.setProductKey(productKey);
            subscribeRelation.setPushMessageType(new ArrayList<>(msgTypes));
            subscribeRelation.setSubscribeFlags(subscribeList.get(0).getSubscribeFlags());
            subscribeRelation.setMnsConfiguration(subscribeList.get(0).getMnsConfiguration());
            return subscribeRelation;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<SubscribeRelation> query(String tenantId, SubscribeFilter filter) throws BusinessException {
        return null;
    }

    //向订阅组增加一个消费组   如果该productKey没有订阅，先调用create创建订阅
    @Override
    public String addSubscribeRelation(String tenantId, ConsumerGroupSubscribeCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(createRq.getGroupId())) {
            throw new BusinessException("消费组ID不能为空！");
        }
        if (StringUtils.isBlank(createRq.getProductKey())) {
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            SubscribeFilter filter = new SubscribeFilter();
            filter.setType(SubscribeRelation.AMQP);
            filter.setProductKey(createRq.getProductKey());
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)) {
                throw new BusinessException("该产品没有订阅，请先创建该产品订阅");
            }
            List<String> msgTypes = subscribeList.stream()
                    .map(PSubscribe::getPushMessageType)
                    .distinct()
                    .collect(Collectors.toList());
            List<PSubscribe> subscribes = new ArrayList<>();
            for (String msgType : msgTypes) {
                PSubscribe pSubscribe = new PSubscribe();
                BeanUtils.copyProperties(subscribeList.get(0), pSubscribe);
                pSubscribe.setUuid(null);
                pSubscribe.setConsumerGroupId(createRq.getGroupId());
                pSubscribe.onCreated(operator);
                pSubscribe.setPushMessageType(msgType);
                subscribes.add(pSubscribe);
            }
            subscribeDao.save(subscribes);
        } catch (Exception e) {
            throw new BusinessException("添加消费组失败：" + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteSubscribeRelation(String tenantId, String groupId, String productKey, String operator, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(groupId)) {
            throw new BusinessException("消费组ID不能为空！");
        }
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            subscribeDao.removeSubscribeByGroupId(tenantId,groupId,productKey);
        } catch (Exception e) {
            throw new BusinessException("删除消费组失败：" + e.getMessage());
        }
    }

}


