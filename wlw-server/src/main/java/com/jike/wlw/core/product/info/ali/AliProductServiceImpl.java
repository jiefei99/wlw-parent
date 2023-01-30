package com.jike.wlw.core.product.info.ali;

import com.aliyun.iot20180120.models.CancelReleaseProductResponse;
import com.aliyun.iot20180120.models.CreateProductResponseBody;
import com.aliyun.iot20180120.models.DeleteProductResponse;
import com.aliyun.iot20180120.models.QueryProductListResponse;
import com.aliyun.iot20180120.models.QueryProductListResponseBody.QueryProductListResponseBodyDataListProductInfo;
import com.aliyun.iot20180120.models.QueryProductResponse;
import com.aliyun.iot20180120.models.ReleaseProductResponse;
import com.aliyun.iot20180120.models.UpdateProductResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.info.ProductConverter;
import com.jike.wlw.core.product.info.ali.iot.IemProductManager;
import com.jike.wlw.dao.TX;
import com.jike.wlw.service.product.info.AliProductFilter;
import com.jike.wlw.service.product.info.AuthType;
import com.jike.wlw.service.product.info.NetType;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.ProtocolType;
import com.jike.wlw.service.product.info.PublishStateType;
import com.jike.wlw.service.product.info.ali.AliProductService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController("productServiceAliImpl")
@ApiModel("阿里产品服务实现")
public class AliProductServiceImpl extends BaseService implements AliProductService {
    @Autowired
    private IemProductManager productManager;

    @Override
    public Product get(String tenantId, String productKey, String iotInstanceId) throws BusinessException {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("产品的ProductKey不能为空");
            }
            QueryProductResponse response = productManager.queryProduct(productKey, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("查询产品失败：" + response.getBody().getErrorMessage());
            }
            if (response.getBody().getData() == null) {
                return null;
            }
            Product result = new Product();
            BeanUtils.copyProperties(response.getBody().getData(), result);
            result.setName(response.getBody().getData().getProductName());
            result.setProductStatus(PublishStateType.map.get(response.getBody().getData().getProductStatus()));
            result.setNetType(NetType.map.get(response.getBody().getData().getNetType()));
            result.setProtocolType(ProtocolType.map.get(response.getBody().getData().getProtocolType()));
            result.setAuthType(AuthType.map.get(response.getBody().getData().getAuthType()));
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public String create(String tenantId, ProductCreateRq createRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");
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
    public void modify(String tenantId, ProductModifyRq modifyRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");
            UpdateProductResponse response = productManager.modifyProduct(modifyRq);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("产品更新失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String productKey, String iotInstanceId, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("需要删除的产品的ProductKey不能为空");
            }
            DeleteProductResponse response = productManager.deleteProduct(productKey, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("产品删除失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Product> query(String tenantId, ProductFilter filter) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(filter, "filter");
            AliProductFilter aliFilter = ProductConverter.aliFilterConver(filter);
            QueryProductListResponse response = productManager.queryProductList(aliFilter);
            List<Product> result = new ArrayList<>();
            if (!response.getBody().getSuccess() || response.getBody().getData() == null || response.getBody().getData().getList() == null) {
                throw new BusinessException("查询产品列表失败：" + response.getBody().getErrorMessage());
            }
            for (QueryProductListResponseBodyDataListProductInfo productInfo : response.getBody().getData().getList().getProductInfo()) {
                Product product = new Product();
                BeanUtils.copyProperties(productInfo, product);
                product.setName(productInfo.getProductName());
                product.setCreated(new Date(productInfo.getGmtCreate()));
                product.setAuthType(AuthType.map.get(product.getAuthType()));
                result.add(product);
            }
            return new PagingResult<>(aliFilter.getCurrentPage(), aliFilter.getPageSize(), response.getBody().getData().getTotal(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void publishProduct(String tenantId, String productKey, String iotInstanceId, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("需要发布的产品的ProductKey不能为空");
            }
            ReleaseProductResponse response = productManager.releaseProduct(productKey, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("产品发布失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void unPublishProduct(String tenantId, String productKey, String iotInstanceId, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("取消发布的产品的ProductKey不能为空");
            }
            CancelReleaseProductResponse response = productManager.cancelReleaseProduct(productKey, iotInstanceId);
            if (!response.getBody().getSuccess()) {
                throw new BusinessException("取消产品发布失败，原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
