package com.jike.wlw.core.product.info;

import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.service.product.info.AliProductFilter;
import com.jike.wlw.service.product.info.AuthType;
import com.jike.wlw.service.product.info.NetType;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.ProtocolType;
import com.jike.wlw.service.product.info.PublishStateType;
import org.apache.commons.lang3.StringUtils;
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

    //转换成P对象
    public static PProduct ProductCover(ProductCreateRq createRq, String tenantId, String productKey, String operator) throws Exception {
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

    //p对象转换成pojo
    public static Product coverProduct(PProduct pProduct){
        Product product=new Product();
        BeanUtils.copyProperties(pProduct,product);
        if (!StringUtil.isNullOrBlank(pProduct.getPhysicalModelIdsJson())) {
            product.setPhysicalModelIds(JsonUtil.jsonToArrayList(pProduct.getPhysicalModelIdsJson(), String.class));
        }
        if (StringUtils.isNotBlank(pProduct.getAuthType())){
            product.setAuthType(AuthType.valueOf(pProduct.getAuthType()));
        }
        if (StringUtils.isNotBlank(pProduct.getNetType())){
            product.setNetType(NetType.valueOf(pProduct.getNetType()));
        }
        if (StringUtils.isNotBlank(pProduct.getProtocolType())){
            product.setProtocolType(ProtocolType.valueOf(pProduct.getProtocolType()));
        }
        if (StringUtils.isNotBlank(pProduct.getProductStatus())){
            product.setProductStatus(PublishStateType.valueOf(pProduct.getProductStatus()));
        }
        return product;
    }

}


