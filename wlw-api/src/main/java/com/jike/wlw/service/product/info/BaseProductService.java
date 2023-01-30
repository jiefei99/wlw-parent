package com.jike.wlw.service.product.info;

import com.geeker123.rumba.commons.paging.PagingResult;

public interface BaseProductService {
    Product get(String tenantId, String productKey, String iotInstanceId);

    String create(String tenantId, ProductCreateRq createRq, String operator);

    void modify(String tenantId, ProductModifyRq modifyRq, String operator);

    void delete(String tenantId, String productKey, String iotInstanceId, String operator);

    PagingResult<Product> query(String tenantId, ProductFilter filter);

    //发布产品
    void publishProduct(String tenantId, String productKey, String iotInstanceId, String operator);

    //取消发布
    void unPublishProduct(String tenantId, String productKey, String iotInstanceId, String operator);
}
