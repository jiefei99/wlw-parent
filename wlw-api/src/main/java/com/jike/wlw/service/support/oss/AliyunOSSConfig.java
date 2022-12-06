/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	commons-oss
 * 文件名：	AliyunOSSConfig.java
 * <p>
 * 2017年1月16日 - 创建。
 */
package com.jike.wlw.service.support.oss;

/**
 * 阿里云OSS配置
 */
public class AliyunOSSConfig {

    private String cdnEnpoint;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;

    public AliyunOSSConfig() {
    }

    public String getCdnEnpoint() {
        return cdnEnpoint;
    }

    public void setCdnEnpoint(String cdnEnpoint) {
        this.cdnEnpoint = cdnEnpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
