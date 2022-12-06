package com.jike.wlw.service.support.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.geeker123.rumba.commons.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/2 15:25- sufengjia - 创建。
 */
@Getter
@Setter
public class OSSService {

    private AliyunOSSConfig config;

    public OssResult getAliOssToken(String modelName) throws BusinessException {
        try {
            String accessId = config.getAccessKeyId(); // 请填写您的AccessKeyId。
            String accessKey = config.getAccessKeySecret(); // 请填写您的AccessKeySecret。
            String endpoint = config.getEndpoint(); // 请填写您的 endpoint。
            String bucket = config.getBucket(); // 请填写您的 bucketname 。
            String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
            // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
//            String callbackUrl = "http://88.88.88.88.:8888";
            String dir = modelName + "/"; // 用户上传文件时指定的前缀。

            OSSClient client = new OSSClient(endpoint, accessId, accessKey);
            long expireTime = 600;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            java.sql.Date expiration = new java.sql.Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            OssResult result = new OssResult();
            result.setAccessid(accessId);
            result.setPolicy(encodedPolicy);
            result.setSignature(postSignature);
            result.setDir(dir);
            result.setHost(host);
            result.setExpire(String.valueOf(expireEndTime / 1000));

            return result;
        } catch (Exception e) {
            throw new BusinessException("存储错误");
        }
    }
}
