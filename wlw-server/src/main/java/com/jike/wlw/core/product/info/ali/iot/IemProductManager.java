package com.jike.wlw.core.product.info.ali.iot;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.CancelReleaseProductRequest;
import com.aliyun.iot20180120.models.CancelReleaseProductResponse;
import com.aliyun.iot20180120.models.CreateProductRequest;
import com.aliyun.iot20180120.models.CreateProductResponse;
import com.aliyun.iot20180120.models.CreateProductResponseBody;
import com.aliyun.iot20180120.models.DeleteProductRequest;
import com.aliyun.iot20180120.models.DeleteProductResponse;
import com.aliyun.iot20180120.models.QueryProductListRequest;
import com.aliyun.iot20180120.models.QueryProductListResponse;
import com.aliyun.iot20180120.models.QueryProductRequest;
import com.aliyun.iot20180120.models.QueryProductResponse;
import com.aliyun.iot20180120.models.ReleaseProductRequest;
import com.aliyun.iot20180120.models.ReleaseProductResponse;
import com.aliyun.iot20180120.models.UpdateProductRequest;
import com.aliyun.iot20180120.models.UpdateProductResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.service.product.info.AliProductFilter;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.ProductQueryRq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class IemProductManager {

    @Autowired
    private AliIotClient client;
//    @Autowired
//    private Environment env;

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config();
        // 您的 AccessKey ID
        config.setAccessKeyId(accessKeyId);
        // 您的 AccessKey Secret
        config.setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        return new Client(config);
    }

    public static void main(String[] args_) throws Exception {
        List<String> args = Arrays.asList(args_);
        // Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
//        CreateProductRequest createProductRequest = new CreateProductRequest();
//        createProductRequest.setNodeType(1);
//        createProductRequest.setProductName("卡卡罗特");
//        createProductRequest.setDataFormat(1);
//        createProductRequest.setDescription("自在极意2");
//        createProductRequest.setAliyunCommodityCode("iothub_senior");
//        createProductRequest.setProtocolType("customize");
//        createProductRequest.setAuthType("secret");
//        createProductRequest.setPublishAuto(true);
//        createProductRequest.setValidateType(1);
//        RuntimeOptions runtime = new RuntimeOptions();
        QueryProductRequest request = new QueryProductRequest();
        request.setProductKey("11111");
        try {
            // 复制代码运行请自行打印 API 的返回值
            QueryProductResponse response = client.queryProduct(request);
            System.out.println("查看结果：" + JSON.toJSONString(response));
        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }
    }

    public CreateProductResponseBody registerProduct(ProductCreateRq registerRq) throws Exception {
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateProductRequest createProductRequest = new CreateProductRequest();
        BeanUtils.copyProperties(registerRq, createProductRequest);
        createProductRequest.setProductName(registerRq.getName());
        createProductRequest.setProtocolType(registerRq.getProtocolType().toString().toLowerCase(Locale.ROOT));
        createProductRequest.setNetType(registerRq.getNetType().toString());
        createProductRequest.setAuthType(registerRq.getAuthType().toString().toLowerCase(Locale.ROOT));
        //todo 暂时写死吧。。。
        createProductRequest.setAliyunCommodityCode("iothub_senior"); //上传此编码支持产品使用物模型，否则不支持
        createProductRequest.setAuthType("secret");  //设备接入物联网认证方式：secret--设备密钥； id2--使用物联网设备身份认证ID²； x509--使用设备X.509证书进行设备身份认证
        createProductRequest.setValidateType(1); // 数据校验级别：1--弱校验，仅校验设备数据的idetifier和dataType字段； 2--免校验，流转全量数据
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            CreateProductResponse productWithOptions = client.createProductWithOptions(createProductRequest, runtime);
            CreateProductResponseBody body = productWithOptions.getBody();
            return body;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public UpdateProductResponse modifyProduct(ProductModifyRq modifyRq) throws Exception {
        if (StringUtils.isBlank(modifyRq.getProductKey())) {
            throw new BusinessException("产品ProductKey不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getName())) {
            throw new BusinessException("产品名称不能为空");
        }
        //        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        BeanUtils.copyProperties(modifyRq, updateProductRequest);
        updateProductRequest.setProductName(modifyRq.getName());
        try {
            UpdateProductResponse updateProductResponse = client.updateProduct(updateProductRequest);
            return updateProductResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public DeleteProductResponse deleteProduct(String productKey, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("需要删除的产品的ProductKey不能为空");
        }
        //        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.setProductKey(productKey);
        deleteProductRequest.setIotInstanceId(iotInstanceId);
        try {
            DeleteProductResponse deleteProductResponse = client.deleteProduct(deleteProductRequest);
            return deleteProductResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    //查询指定产品的详细信息
    public QueryProductResponse queryProduct(String productKey, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("产品的ProductKey不能为空");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryProductRequest request = new QueryProductRequest();
        request.setProductKey(productKey);
        request.setIotInstanceId(iotInstanceId);
        try {
            QueryProductResponse queryProductResponse = client.queryProduct(request);
            return queryProductResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    //查看产品列表
    public QueryProductListResponse queryProductList(AliProductFilter filter) throws Exception {
        if (filter.getCurrentPage() < 1) {
            throw new BusinessException("页数默认从第一页开始显示");
        }
        if (filter.getPageSize() < 1 || filter.getPageSize() > 200) {
            throw new BusinessException("每页显示的产品数量不能超过200个");
        }
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        QueryProductListRequest request = new QueryProductListRequest();
        BeanUtils.copyProperties(filter, request);
        try {
            QueryProductListResponse queryProductListResponse = client.queryProductList(request);
            return queryProductListResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public ReleaseProductResponse releaseProduct(String productKey, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("产品的ProductKey不能为空");
        }
        ReleaseProductRequest request = new ReleaseProductRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setProductKey(productKey);
        try {
            ReleaseProductResponse response = client.releaseProduct(request);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public CancelReleaseProductResponse cancelReleaseProduct(String productKey, String iotInstanceId) throws Exception {
        if (StringUtils.isBlank(productKey)) {
            throw new BusinessException("产品的ProductKey不能为空");
        }
        CancelReleaseProductRequest request = new CancelReleaseProductRequest();
        request.setIotInstanceId(iotInstanceId);
        request.setProductKey(productKey);
        try {
            CancelReleaseProductResponse response = client.cancelReleaseProduct(request);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
