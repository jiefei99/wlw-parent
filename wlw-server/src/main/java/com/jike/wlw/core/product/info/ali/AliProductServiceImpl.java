package com.jike.wlw.core.product.info.ali;

import com.aliyun.iot20180120.models.CreateProductResponseBody;
import com.aliyun.iot20180120.models.DeleteProductResponse;
import com.aliyun.iot20180120.models.QueryProductListResponse;
import com.aliyun.iot20180120.models.QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo;
import com.aliyun.iot20180120.models.QueryProductResponse;
import com.aliyun.iot20180120.models.UpdateProductResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.info.ali.iot.IemProductManager;
import com.jike.wlw.dao.TX;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.ProductQueryRq;
import com.jike.wlw.service.product.info.ali.AliProductService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController("productServiceAliImpl")
@ApiModel("阿里产品服务实现")
public class AliProductServiceImpl extends BaseService implements AliProductService {
    @Autowired
    private IemProductManager productManager;

    @Override
    public Product get(String tenantId,ProductQueryRq productQueryRq) throws BusinessException {
        try {
            QueryProductResponse response = productManager.queryProduct(productQueryRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("查询产品失败："+response.getBody().getErrorMessage());
            }
            if (response.getBody().getData()==null){
                return null;
            }
            Product result = new Product();
            BeanUtils.copyProperties(response.getBody().getData(), result);
            result.setName(response.getBody().getData().getProductName());
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public String create(String tenantId,ProductCreateRq createRq, String operator) throws BusinessException {
        try {
            CreateProductResponseBody response = productManager.registerProduct(createRq);
            if (!response.getSuccess()) {
                throw new BusinessException("新建产品失败，原因：" + response.getErrorMessage());
            }
            //阿里无uuid返回productKey
            return response.getProductKey();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId,ProductModifyRq modifyRq, String operator) throws BusinessException {
        try {
            UpdateProductResponse response = productManager.modifyProduct(modifyRq);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("产品更新失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String productKey, String iotInstanceId) throws BusinessException {
        try {

            DeleteProductResponse response = productManager.deleteProduct(productKey,iotInstanceId);
            if (!response.getBody().getSuccess()){
                throw new BusinessException("产品删除失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Product> query(String tenantId,ProductQueryRq productQueryRq) throws BusinessException {
        try {
            QueryProductListResponse response = productManager.queryProductList(productQueryRq);
            List<Product> result = new ArrayList<>();
            if (!response.getBody().getSuccess()||response.getBody().getData().getList()==null){
                throw new BusinessException("查询产品列表失败："+response.getBody().getErrorMessage());
            }
            for (QueryProductListResponseBodyDataListProductInfo productInfo : response.getBody().getData().getList().getProductInfo()) {
                Product product = new Product();
                BeanUtils.copyProperties(productInfo, product);
                product.setName(productInfo.getProductName());
                result.add(product);
            }
            return new PagingResult<>(productQueryRq.getCurrentPage(), productQueryRq.getPageSize(), response.getBody().getData().getTotal(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
