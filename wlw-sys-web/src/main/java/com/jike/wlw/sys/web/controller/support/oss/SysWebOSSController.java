package com.jike.wlw.sys.web.controller.support.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.oss.CloudStorageConfig;
import com.geeker123.rumba.oss.CloudStorageType;
import com.geeker123.rumba.oss.OSSFactory;
import com.jike.wlw.service.config.Config;
import com.jike.wlw.service.config.WlwConfig;
import com.jike.wlw.sys.web.config.fegin.ConfigFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2022，所有权利保留。
 * <p>
 * 修改历史：
 * 2022/12/23 13:48- zhengzhoudong - 创建。
 */
@Api(value = "阿里云OSS服务", tags = {"阿里云OSS服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/oss", produces = "application/json;charset=utf-8")
public class SysWebOSSController extends BaseController {

    @Autowired
    private ConfigFeignClient configFeignClient;

    @ApiOperation(value = "获取信息")
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Map> getToken(@ApiParam(required = true, value = "文件夹ID") @RequestParam(value = "ossFileId") String ossFileId) {
        try {
            Config perz = configFeignClient.get(WlwConfig.GROUP_OSS, WlwConfig.KEY_OSS_CONFIG_KEY);
            if (perz == null) {
                return ActionResult.fail("未配置云存储配置项");
            }

            Map<String, String> result = new HashMap<>();
            CloudStorageConfig config = JsonUtil.jsonToObject(perz.getConfigValue(), CloudStorageConfig.class);
            if (config.getType() == CloudStorageType.ALIYUN.getValue()) {
                String bucket = config.getAliyunBucketName(); // 请填写您的 bucketname 。
                String dir = ossFileId + "/"; // 用户上传文件时指定的前缀。
                String aliyunDomain = config.getAliyunDomain();

                OSS client = new OSSClientBuilder().build(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(), config.getAliyunAccessKeySecret());
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

                result.put("accessid", config.getAliyunAccessKeyId());
                result.put("policy", encodedPolicy);
                result.put("signature", postSignature);
                result.put("dir", ossFileId);
                result.put("host", StringUtil.isNullOrBlank(aliyunDomain) ? bucket + "." + config.getAliyunEndPoint() : aliyunDomain);
                result.put("expire", String.valueOf(expireEndTime / 1000));
            } else if (config.getType() == CloudStorageType.QINIU.getValue()) {
                String token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).uploadToken(config.getQiniuBucketName(), null, 72000, new StringMap());
                result.put("qiniuHost", config.getQiniuDomain());
                result.put("token", token);
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取云存储配置项")
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<CloudStorageConfig> getConfig() throws BusinessException {
        try {
            Config perz = configFeignClient.get(WlwConfig.GROUP_OSS, WlwConfig.KEY_OSS_CONFIG_KEY);
            if (perz == null) {
                return ActionResult.ok();
            }
            CloudStorageConfig result = JsonUtil.jsonToObject(perz.getConfigValue(), CloudStorageConfig.class);

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation("保存云存储配置项")
    @RequestMapping(value = "/saveConfig", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> saveConfig(@ApiParam(required = true, value = "云存储配置信息") @RequestBody CloudStorageConfig config) throws BusinessException {
        try {
            Config perz = configFeignClient.get(WlwConfig.GROUP_OSS, WlwConfig.KEY_OSS_CONFIG_KEY);
            if (perz == null) {
                perz = new Config();
                perz.setConfigGroup(WlwConfig.GROUP_OSS);
                perz.setConfigKey(WlwConfig.KEY_OSS_CONFIG_KEY);
                perz.setConfigName("云存储配置项");
            }

            perz.setConfigValue(JsonUtil.objectToJson(config));
            String result = configFeignClient.save(perz, getUserName());

            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation("上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> upload(@ApiParam(required = true, value = "文件") @RequestBody MultipartFile file,
                                       @ApiParam(value = "path", required = true) @RequestParam(value = "path") String path) throws BusinessException {
        try {
            if (file == null || file.isEmpty()) {
                return ActionResult.fail("上传文件不能为空");
            }

            Config perz = configFeignClient.get(WlwConfig.GROUP_OSS, WlwConfig.KEY_OSS_CONFIG_KEY);
            if (perz == null) {
                return ActionResult.ok();
            }
            CloudStorageConfig config = JsonUtil.jsonToObject(perz.getConfigValue(), CloudStorageConfig.class);

            // 上传文件
            String url = OSSFactory.build(config).upload(file.getBytes(), path);
            return ActionResult.ok(url);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
