package com.jike.wlw.core.product.topic.ali;

import com.aliyun.iot20180120.models.CreateProductResponseBody;
import com.aliyun.iot20180120.models.CreateProductTopicResponse;
import com.aliyun.iot20180120.models.DeleteProductTopicResponse;
import com.aliyun.iot20180120.models.QueryProductTopicResponse;
import com.aliyun.iot20180120.models.QueryProductTopicResponseBody.QueryProductTopicResponseBodyDataProductTopicInfo;
import com.aliyun.iot20180120.models.UpdateProductTopicResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.topic.ali.iot.TopicManager;
import com.jike.wlw.service.product.topic.Topic;
import com.jike.wlw.service.product.topic.TopicCreateRq;
import com.jike.wlw.service.product.topic.TopicFilter;
import com.jike.wlw.service.product.topic.TopicModifyRq;
import com.jike.wlw.service.product.topic.ali.AliTopicService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AliTopicServiceImpl extends BaseService implements AliTopicService {

    @Autowired
    private TopicManager topicManager;
    @Override
    public List<Topic> query(String tenantId, TopicFilter filter) throws BusinessException {
        try {
            QueryProductTopicResponse response = topicManager.queryProductTopic(filter);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("查询Topic失败，原因：" + response.getBody().getErrorMessage());
            }
            List<Topic> topicList=new ArrayList<>();
            if (CollectionUtils.isEmpty(response.getBody().getData().getProductTopicInfo())){
                return topicList;
            }
            for (QueryProductTopicResponseBodyDataProductTopicInfo info : response.getBody().getData().getProductTopicInfo()) {
                Topic topic=new Topic();
                topic.setTopicId(info.getId());
                topic.setDesc(info.getDesc());
                topic.setTopicShortName(info.getTopicShortName());
//                topic.setOperation();//info.getOperation()
                topic.setProductKey(info.getProductKey());
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
    public void delete(String tenantId, String topicId, String iotInstanceId) throws BusinessException {
        try {
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


