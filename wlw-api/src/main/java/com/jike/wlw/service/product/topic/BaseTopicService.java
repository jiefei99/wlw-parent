package com.jike.wlw.service.product.topic;

import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.ProductQueryRq;

import java.util.List;

public interface BaseTopicService {
    List<Topic> query(String tenantId, TopicFilter filter);

    String create(String tenantId, TopicCreateRq createRq, String operator);

    void modify(String tenantId, TopicModifyRq modifyRq, String operator);

    void delete(String tenantId, String topicId, String iotInstanceId, String operator);
}
