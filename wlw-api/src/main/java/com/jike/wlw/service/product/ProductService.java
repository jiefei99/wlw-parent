package com.jike.wlw.service.product;

import com.geeker123.rumba.commons.paging.PagingResult;

public interface ProductService {
    Product get(String tenantId,ProductQueryRq productQueryRq);

    String create(String tenantId,ProductCreateRq createRq,String operator);

    void modify(String tenantId,ProductModifyRq modifyRq,String operator);

    void delete(String tenantId,String productKey,String iotInstanceId);

    PagingResult<Product> query(String tenantId,ProductQueryRq productQueryRq);

}
