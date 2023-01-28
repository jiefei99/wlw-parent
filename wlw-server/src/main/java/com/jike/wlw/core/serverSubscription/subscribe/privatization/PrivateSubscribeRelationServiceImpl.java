package com.jike.wlw.core.serverSubscription.subscribe.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.dao.product.info.ProductDao;
import com.jike.wlw.dao.serverSubscription.consumerGroup.PConsumerGroup;
import com.jike.wlw.dao.serverSubscription.subscribe.PSubscribe;
import com.jike.wlw.dao.serverSubscription.subscribe.SubscribeDao;
import com.jike.wlw.service.product.topic.Operation;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroup;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            List<PSubscribe> list = new ArrayList<>();
            for (String info : createRq.getPushMessageType()) {
                PSubscribe subscribe = new PSubscribe();
                subscribe.setTenantId(tenantId);
                subscribe.setType(createRq.getType());
                if (CollectionUtils.isNotEmpty(createRq.getConsumerGroupIds())) {
                    subscribe.setConsumerGroupIds(createRq.getConsumerGroupIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                subscribe.setProductKey(createRq.getProductKey());
                if (StringUtils.isNotBlank(createRq.getMnsConfiguration())) {
                    subscribe.setMnsConfiguration(createRq.getMnsConfiguration());
                }
                subscribe.setSubscribeFlags(createRq.getSubscribeFlags());
                subscribe.setPushMessageType(info);
                subscribe.onCreated(operator);
                list.add(subscribe);
            }
            subscribeDao.save(list);
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
            subscribeDao.removeSubscribe(modifyRq.getType(),modifyRq.getProductKey(),tenantId);
            List<PSubscribe> subscribeList = new ArrayList<>();
            for (String info : modifyRq.getPushMessageType()) {
                PSubscribe subscribe = new PSubscribe();
                subscribe.setType(modifyRq.getType());
                if (CollectionUtils.isNotEmpty(modifyRq.getConsumerGroupIds())) {
                    subscribe.setConsumerGroupIds(modifyRq.getConsumerGroupIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                subscribe.setTenantId(tenantId);
                if (StringUtils.isNotBlank(modifyRq.getMnsConfiguration())) {
                    subscribe.setMnsConfiguration(modifyRq.getMnsConfiguration());
                }
                subscribe.setProductKey(modifyRq.getProductKey());
                subscribe.setSubscribeFlags(modifyRq.getSubscribeFlags());
                subscribe.setPushMessageType(info);
                subscribe.onCreated(operator);
                subscribeList.add(subscribe);
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
            subscribeDao.removeSubscribe(type,productKey,tenantId);
            //数据留着没有太大意义，暂时不用逻辑删
//            List<PSubscribe> subscribeList = subscribeDao.query(filter);
//            for (PSubscribe info : subscribeList) {
//                info.setIsDeleted(1);
//            }
//            subscribeDao.save(subscribeList);
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
            filter.setPage(filter.getPage());
            filter.setPageSize(filter.getPageSize() <= 0 ? 100 : filter.getPageSize());
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)) {
                return null;
            }
            List<String> amqpMsgTypeList = new ArrayList<>();
            for (PSubscribe subscribe : subscribeList) {
                amqpMsgTypeList.add(subscribe.getPushMessageType());
            }
            SubscribeRelation subscribeRelation = new SubscribeRelation();
            subscribeRelation.setType(type);
            subscribeRelation.setConsumerGroupIds(Arrays.asList(subscribeList.get(0).getConsumerGroupIds().split(",")));
            subscribeRelation.setProductKey(productKey);
            subscribeRelation.setPushMessageType(amqpMsgTypeList);
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
//        try {
//            filter.setTenantId(tenantId);
//            List<PSubscribe> subscribeList = subscribeDao.page(filter);
//            List<SubscribeRelation> result = new ArrayList<>();
//            for (PSubscribe pSubscribe : subscribeList) {
//            }
//            long count = subscribeDao.getCount(filter);
//            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new BusinessException(e.getMessage(), e);
//        }
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
                return null;
            }
            List<String> groupIds =new ArrayList<>(Arrays.asList(subscribeList.get(0).getConsumerGroupIds().split(",")));
            if (groupIds.contains(createRq.getGroupId())) {
                return null;
            }
            groupIds.add(createRq.getGroupId());
            for (PSubscribe subscribe : subscribeList) {
                subscribe.onModified(operator);
                subscribe.setConsumerGroupIds(groupIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
            }
            subscribeDao.save(subscribeList);
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
            SubscribeFilter filter = new SubscribeFilter();
            filter.setType(SubscribeRelation.AMQP);
            filter.setProductKey(productKey);
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)) {
                return;
            }
            String consumerGroupIds = subscribeList.get(0).getConsumerGroupIds();
            List<String> groupIds = new ArrayList<>( Arrays.asList(consumerGroupIds.split(",")));
            if (!groupIds.contains(groupId) || groupIds.size() == 1) {
                return;
            }
            IntStream.range(0, groupIds.size()).filter(i -> groupIds.get(i).equals(groupId)).
                    boxed().findFirst().map(i -> groupIds.remove((int) i));
            for (PSubscribe subscribe : subscribeList) {
                subscribe.onModified(operator);
                subscribe.setConsumerGroupIds(groupIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
            }
            subscribeDao.save(subscribeList);
        } catch (Exception e) {
            throw new BusinessException("删除消费组失败：" + e.getMessage());
        }
    }

}


