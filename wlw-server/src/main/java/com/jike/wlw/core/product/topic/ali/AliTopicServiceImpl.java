package com.jike.wlw.core.product.topic.ali;

import com.aliyun.iot20180120.models.CreateProductTopicResponse;
import com.aliyun.iot20180120.models.DeleteProductTopicResponse;
import com.aliyun.iot20180120.models.QueryProductTopicResponse;
import com.aliyun.iot20180120.models.QueryProductTopicResponseBody.QueryProductTopicResponseBodyDataProductTopicInfo;
import com.aliyun.iot20180120.models.UpdateProductTopicResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.topic.ali.iot.TopicManager;
import com.jike.wlw.service.product.topic.Operation;
import com.jike.wlw.service.product.topic.Topic;
import com.jike.wlw.service.product.topic.TopicCreateRq;
import com.jike.wlw.service.product.topic.TopicFilter;
import com.jike.wlw.service.product.topic.TopicModifyRq;
import com.jike.wlw.service.product.topic.ali.AliTopicService;
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
 * @title: AliTopicServiceImpl
 * @Author RS
 * @Date: 2023/1/14 16:09
 * @Version 1.0
 */

@Slf4j
@RestController("topicServiceAliImpl")
@ApiModel("阿里Topic服务实现")
@RequestMapping(value = "service/aliTopic", produces = "application/json;charset=utf-8")
public class AliTopicServiceImpl extends BaseService implements AliTopicService {

    @Autowired
    private TopicManager topicManager;

    @Override
    public List<Topic> query(String tenantId, TopicFilter filter) throws BusinessException {
        try {
            if (StringUtils.isBlank(filter.getProductKey())){
                throw new IllegalAccessException("Topic类的productKey不能为空");
            }
            QueryProductTopicResponse response = topicManager.queryProductTopic(filter.getProductKey(),filter.getIotInstanceId());
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("查询Topic失败，原因：" + response.getBody().getErrorMessage());
            }
            List<Topic> topicList = new ArrayList<>();
            if (CollectionUtils.isEmpty(response.getBody().getData().getProductTopicInfo())) {
                return topicList;
            }
            for (QueryProductTopicResponseBodyDataProductTopicInfo info : response.getBody().getData().getProductTopicInfo()) {
                Topic topic = new Topic();
                topic.setId(info.getId());
                topic.setDesc(info.getDesc());
                topic.setTopicShortName(info.getTopicShortName());
                if ("0".equals(info.getOperation())) {
                    topic.setOperation(Operation.SUB);
                } else if ("1".equals(info.getOperation())) {
                    topic.setOperation(Operation.PUB);
                } else {
                    topic.setOperation(Operation.ALL);
                }
                topic.setProductKey(info.getProductKey());
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
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");
            CreateProductTopicResponse response = topicManager.createProductTopic(createRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("新建Topic失败，原因：" + response.getBody().getErrorMessage());
            }
            return response.getBody().getTopicId().toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, TopicModifyRq modifyRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");
            UpdateProductTopicResponse response = topicManager.updateProductTopic(modifyRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("修改Topic失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String topicId, String iotInstanceId, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(topicId)){
                throw new IllegalAccessException("修改的Topic类的ID不能为空");
            }
            DeleteProductTopicResponse response = topicManager.deleteProductTopic(topicId, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("删除Topic失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}


