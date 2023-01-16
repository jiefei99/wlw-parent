package com.jike.wlw.core.product.serverSubscription.subscribe.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.product.serverSubscription.subscribe.PSubscribe;
import com.jike.wlw.dao.product.serverSubscription.subscribe.SubscribeDao;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.product.serverSubscription.subscribe.SubscribeFilter;
import com.jike.wlw.service.product.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.product.serverSubscription.subscribe.SubscribeRelationCreateRq;
import com.jike.wlw.service.product.serverSubscription.subscribe.SubscribeRelationModifyRq;
import com.jike.wlw.service.product.serverSubscription.subscribe.privatization.PrivateSubscribeRelationService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public String create(String tenantId, SubscribeRelationCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(createRq.getType())){
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(createRq.getProductKey())){
            throw new BusinessException("ProductKey不能为空！");
        }
        if ("MNS".equals(createRq.getType())&& io.micrometer.core.instrument.util.StringUtils.isBlank(createRq.getMnsConfiguration())){
            throw new BusinessException("MNS队列的配置信息不能为空");
        }
        //consumerGroupIds
        if ("AMQP".equals(createRq.getType())&& CollectionUtils.isEmpty(createRq.getConsumerGroupIds())){
            throw new BusinessException("创建的AMQP订阅中的消费组ID不能为空");
        }
        if (CollectionUtils.isEmpty(createRq.getPushMessageType())){
            throw new BusinessException("订阅消息不能为空");
        }
        try {
            List<PSubscribe> list=new ArrayList<>();
            for (String info : createRq.getPushMessageType()) {
                PSubscribe subscribe=new PSubscribe();
                subscribe.setId(StringRelevant.buildId(20));
                subscribe.setTenantId(tenantId);
                subscribe.setType(createRq.getType());
                if (CollectionUtils.isNotEmpty(createRq.getConsumerGroupIds())){
                    subscribe.setConsumerGroupIds(createRq.getConsumerGroupIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                subscribe.setMnsConfiguration(createRq.getMnsConfiguration());
                subscribe.setSubscribeFlags(createRq.getSubscribeFlags());
                subscribe.setPushMessageType(info);
                list.add(subscribe);
            }
            subscribeDao.save(list);
            return null;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, SubscribeRelationModifyRq modifyRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(modifyRq.getType())){
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(modifyRq.getProductKey())){
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            //生成P对象，写进数据库，先查这个productkey和type有没有订阅，有才能修改
            SubscribeFilter filter=new SubscribeFilter();
            filter.setType(modifyRq.getType());
            filter.setProductKey(modifyRq.getProductKey());
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            for (PSubscribe subscribe : subscribeList) {
                subscribe.setMnsConfiguration(modifyRq.getMnsConfiguration());
                subscribe.setSubscribeFlags(modifyRq.getSubscribelags());
                subscribe.setConsumerGroupIds(modifyRq.getConsumerGroupIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
                subscribe.setPushMessageType(modifyRq.getPushMessageType().stream().map(String::valueOf).collect(Collectors.joining(",")));
            }
            subscribeDao.save(subscribeList);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String productKey, String type, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(type)){
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            SubscribeFilter filter=new SubscribeFilter();
            filter.setType(type);
            filter.setProductKey(productKey);
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            for (PSubscribe info : subscribeList) {
                info.setIsDeleted(1);
            }
            //逻辑删
            subscribeDao.save(subscribeList);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public SubscribeRelation get(String tenantId, String productKey, String type, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(type)){
            throw new BusinessException("订阅类型不能为空！");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            SubscribeFilter filter=new SubscribeFilter();
            filter.setType(type);
            filter.setProductKey(productKey);
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)){
                return null;
            }
            List<String> pushMsgTypeList=new ArrayList<>();
            for (PSubscribe subscribe : subscribeList) {
                pushMsgTypeList.add(subscribe.getPushMessageType());
            }
            SubscribeRelation subscribeRelation=new SubscribeRelation();
            subscribeRelation.setType(type);
            subscribeRelation.setProductKey(productKey);
            subscribeRelation.setPushMessageType(pushMsgTypeList);
            subscribeRelation.setSubscribeFlags(subscribeList.get(0).getSubscribeFlags());
            subscribeRelation.setMnsConfiguration(subscribeList.get(0).getMnsConfiguration());
            return subscribeRelation;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public String createSubscribeRelation(String tenantId, ConsumerGroupSubscribeCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(createRq.getGroupId())){
            throw new BusinessException("消费组ID不能为空！");
        }
        if (StringUtils.isBlank(createRq.getProductKey())){
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            SubscribeFilter filter=new SubscribeFilter();
            filter.setType(SubscribeRelation.AMQP);
            filter.setProductKey(createRq.getProductKey());
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)){
                return null;
            }
            List<String> groupIds = Arrays.asList(subscribeList.get(0).getConsumerGroupIds().split(","));
            if (groupIds.contains(createRq.getGroupId())){
                return null;
            }
            groupIds.add(createRq.getGroupId());
            for (PSubscribe subscribe : subscribeList) {
                subscribe.setConsumerGroupIds(groupIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
            }
            subscribeDao.save(subscribeList);
        } catch (Exception e) {
            throw new BusinessException("添加消费组失败："+e.getMessage());
        }
        return null;
        //endregion
    }

    @Override
    public void deleteSubscribeRelation(String tenantId, String groupId, String productKey, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(groupId)) {
            throw new BusinessException("消费组ID不能为空！");
        }
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("ProductKey不能为空！");
        }
        try {
            SubscribeFilter filter=new SubscribeFilter();
            filter.setType(SubscribeRelation.AMQP);
            filter.setProductKey(productKey);
            filter.setTenantId(tenantId);
            List<PSubscribe> subscribeList = subscribeDao.query(filter);
            if (CollectionUtils.isEmpty(subscribeList)){
                return;
            }
            String consumerGroupIds = subscribeList.get(0).getConsumerGroupIds();
            if (!consumerGroupIds.contains(groupId)){
                return;
            }
            List<String> groupIds = Arrays.asList(consumerGroupIds.split(","));
            IntStream.range(0,groupIds.size()).filter(i-> groupIds.get(i).equals(groupId)).
                    boxed().findFirst().map(i->groupIds.remove((int)i));
            if (groupIds.size()==0){
                for (PSubscribe subscribe : subscribeList) {
                    subscribe.setIsDeleted(1);
                }
            }else{
                for (PSubscribe subscribe : subscribeList) {
                    subscribe.setConsumerGroupIds(groupIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
            }
            subscribeDao.save(subscribeList);
        } catch (Exception e) {
            throw new BusinessException("添加消费组失败："+e.getMessage());
        }
    }

}


