package com.jike.wlw.core.product.topic.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.dao.product.info.ProductDao;
import com.jike.wlw.dao.product.topic.PTopic;
import com.jike.wlw.dao.product.topic.TopicDao;
import com.jike.wlw.service.product.topic.Operation;
import com.jike.wlw.service.product.topic.Topic;
import com.jike.wlw.service.product.topic.TopicCreateRq;
import com.jike.wlw.service.product.topic.TopicFilter;
import com.jike.wlw.service.product.topic.TopicModifyRq;
import com.jike.wlw.service.product.topic.privatization.PrivateTopicService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @title: PrivateTopicServiceImpl
 * @Author RS
 * @Date: 2023/1/14 16:14
 * @Version 1.0
 */
@Slf4j
@RestController("topicServicePrivateImpl")
@ApiModel("私有化Topic服务实现")
public class PrivateTopicServiceImpl extends BaseService implements PrivateTopicService {
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Topic> query(String tenantId, TopicFilter filter) throws BusinessException {
        if (StringUtils.isBlank(filter.getProductKey())) {
            throw new BusinessException("ProductKey不能为空！");
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        try {
            filter.setTenantId(tenantId);
            List<PTopic> topics = topicDao.get(filter);
            if (CollectionUtils.isEmpty(topics)) {
                return null;
            }
            List<Topic> topicList = new ArrayList<>();
            for (PTopic info : topics) {
                Topic topic = new Topic();
                BeanUtils.copyProperties(info, topic);
                topic.setDesc(info.getDetails());
                topic.setOperation(Operation.valueOf(info.getOperation()));
                topicList.add(topic);
            }
            return topicList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public String create(String tenantId, TopicCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        if (createRq.getOperation() == null) {
            throw new BusinessException("Topic操作权限不能为空！");
        }
        if (StringUtils.isBlank(createRq.getProductKey())) {
            throw new BusinessException("ProductKey不能为空！");
        }
        if (StringUtils.isBlank(createRq.getTopicShortName())) {
            throw new BusinessException("自定义类目名称不能为空！");
        }
        try {
            PProduct perz = productDao.get(PProduct.class, "productKey", createRq.getProductKey(), "tenantId", tenantId, "isDeleted", 0);
            if (perz == null) {
                throw new BusinessException("查无此产品！");
            }
            PTopic topic = new PTopic();
            topic.setDetails(createRq.getDesc());
            topic.setProductKey(createRq.getProductKey());
            topic.setOperation(createRq.getOperation().toString());
            topic.setId(StringRelevant.buildId(20));
            topic.setTopicShortName(createRq.getTopicShortName());
            topic.setTenantId(tenantId);
            topic.onCreated(operator);
            topicDao.save(topic);
            return topic.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, TopicModifyRq modifyRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(modifyRq.getId())) {
            throw new BusinessException("Topic Id不能为空！");
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(modifyRq.getTopicShortName())) {
            throw new BusinessException("Topic类的自定义类目名称不能为空！");
        }
        if (modifyRq.getOperation() == null) {
            throw new BusinessException("Topic类的操作权限不能为空！");
        }
        try {
            PTopic topic = topicDao.get(PTopic.class, "id", modifyRq.getId(), "tenantId", tenantId);
            topic.setTopicShortName(modifyRq.getTopicShortName());
            topic.setOperation(modifyRq.getOperation().toString());
            topic.setDetails(modifyRq.getDesc());
            topic.onModified(operator);
            topicDao.save(topic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.out.println(e.getMessage());
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String topicId, String iotInstanceId, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(topicId)) {
            throw new BusinessException("Topic Id不能为空！");
        }
        try {
            TopicFilter filter = new TopicFilter();
            PTopic topic = topicDao.get(PTopic.class, "id", topicId, "tenantId", tenantId);
            if (topic == null) {
                return;
            }
            topic.setIsDeleted(1);
            topic.onModified(operator);
            topicDao.save(topic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

}


