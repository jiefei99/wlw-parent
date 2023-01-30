package com.jike.wlw.service.product.info;

import com.geeker123.rumba.commons.paging.PagingResult;

public interface BaseProductService {
    //ReleaseProduct
    //CancelReleaseProduct
    Product get(String tenantId, ProductQueryRq productQueryRq);

    String create(String tenantId, ProductCreateRq createRq, String operator);

    void modify(String tenantId, ProductModifyRq modifyRq, String operator);

    void delete(String tenantId,String productKey,String iotInstanceId);

    PagingResult<Product> query(String tenantId,ProductQueryRq productQueryRq);

    //发布产品
    void releaseProduct(String tenantId,String productKey,String iotInstanceId, String operator);
    //取消发布
    void cancelReleaseProduct(String tenantId,String productKey,String iotInstanceId, String operator);

}
