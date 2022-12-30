package com.jike.wlw.core.product.iot;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.service.product.ProductModifyRq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class IemProductManager {

    @Autowired
    private Environment env;

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
//        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setNodeType(1);
        createProductRequest.setProductName("超级赛亚人004");
        createProductRequest.setDataFormat(1);
        createProductRequest.setDescription("变身超级赛亚人");
        createProductRequest.setAliyunCommodityCode("iothub_senior");
        createProductRequest.setProtocolType("customize");
        createProductRequest.setAuthType("secret");
        createProductRequest.setPublishAuto(true);
        createProductRequest.setValidateType(1);
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            CreateProductResponse productWithOptions = client.createProductWithOptions(createProductRequest, runtime);
            System.out.println("新建产品返回结果：" + productWithOptions);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
        }
    }

    public CreateProductResponseBody registerProduct(RegisterProductRq registerRq) throws Exception {
        //TODO id和secret之后需要从表获取或者从env配置获取
//        Client client = createClient("LTAIZOpGhq6KtGqU", "xi2neJPmjJqDOmtjzTL9pBq8yLXogZ");
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setNodeType(registerRq.getNodeType());
        createProductRequest.setProductName(registerRq.getProductName());
        createProductRequest.setDataFormat(registerRq.getDataFormat());
        createProductRequest.setDescription(registerRq.getDescription());
        createProductRequest.setProtocolType(registerRq.getProtocolType().getCaption());
        createProductRequest.setAliyunCommodityCode("iothub_senior"); //上传此编码支持产品使用物模型，否则不支持
        createProductRequest.setAuthType("secret");  //设备接入物联网认证方式：secret--设备密钥； id2--使用物联网设备身份认证ID²； x509--使用设备X.509证书进行设备身份认证
        createProductRequest.setPublishAuto(true); //是否创建产品自动发布物模型
        createProductRequest.setValidateType(1); // 数据校验级别：1--弱校验，仅校验设备数据的idetifier和dataType字段； 2--免校验，流转全量数据
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            CreateProductResponse productWithOptions = client.createProductWithOptions(createProductRequest, runtime);
            CreateProductResponseBody body = productWithOptions.getBody();
//            if (!body.getSuccess() && !StringUtil.isNullOrBlank(body.getErrorMessage())) {
//                throw new BusinessException(body.getErrorMessage());
//            }

            return body;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public UpdateProductResponse modifyProduct(ProductModifyRq modifyRq) throws Exception {
        com.aliyun.iot20180120.Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setProductKey(modifyRq.getProductKey()); //必填
        updateProductRequest.setProductName(modifyRq.getName()); //必填
        updateProductRequest.setDescription(modifyRq.getDescription());
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            UpdateProductResponse updateProductResponse = client.updateProductWithOptions(updateProductRequest, runtime);

            return updateProductResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public DeleteProductResponse deleteProduct(String id) throws Exception {
        com.aliyun.iot20180120.Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.setProductKey(id);
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            DeleteProductResponse deleteProductResponse = client.deleteProductWithOptions(deleteProductRequest, runtime);

            return deleteProductResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
