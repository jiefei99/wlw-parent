package com.jike.wlw.core.product.info;

import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.service.product.info.AliProductFilter;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.PublishStateType;
import org.springframework.beans.BeanUtils;

/**
 * @title: ProductConverter
 * @Author RS
 * @Date: 2023/1/30 10:41
 * @Version 1.0
 */
public class ProductConverter {
    public static AliProductFilter aliFilterConver(ProductFilter filter){
        AliProductFilter aliFilter=new AliProductFilter();
        aliFilter.setCurrentPage(filter.getPage());
        aliFilter.setPageSize(filter.getPageSize());
        aliFilter.setResourceGroupId(filter.getResourceGroupId());
        aliFilter.setAliyunCommodityCode(filter.getAliyunCommodityCode());
        aliFilter.setIotInstanceId(filter.getIotInstanceId());
        return aliFilter;
    }
    public static PProduct coverPProduct(ProductCreateRq createRq,String tenantId,String productKey,String operator) throws Exception {
        PProduct pProduct=new PProduct();
        BeanUtils.copyProperties(createRq, pProduct);
        pProduct.setAuthType(createRq.getAuthType().toString());
        pProduct.setId(StringRelevant.buildId(20));
        pProduct.setTenantId(tenantId);
        pProduct.setProductKey(productKey);
        pProduct.setNetType(createRq.getNetType().toString());
        pProduct.setProductStatus(createRq.isPublishAuto()? PublishStateType.RELEASE_STATUS.toString():PublishStateType.DEVELOPMENT_STATUS.toString());
        pProduct.setProductSecret(StringRelevant.buildId(16));
        pProduct.onCreated(operator);
        return pProduct;
    }
}


