/**
 * 版权所有(C)，极客软创（厦门）信息技术有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	missyou-api
 * 文件名：	HwsConfig.java
 * 模块说明：
 * 修改历史：
 * <p>
 * 2018年3月4日 - lsz - 创建。
 */
package com.jike.wlw.service.config;


import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("配置")
public class WlwConfig extends StandardEntity {
    private static final long serialVersionUID = -4465201274239207980L;

//    /**
//     * 分组：微信授权登录配置组
//     */
//    public static final String GROUP_LOGIN_JS = "wechat.login.js";
//    public static final String KEY_LOGIN_JS_APPID = "login.js.appId";
//    public static final String KEY_LOGIN_JS_SECRET = "login.js.secret";
//    public static final String KEY_LOGIN_JS_CALLBACK_URL = "login.js.callback.url";
//    public static final String KEY_LOGIN_JS_ACCESS_TOKEN = "login.js.access.token";

    /**
     * 分组：阿里云OSS对象存储
     */
    public static final String GROUP_OSS = "wlw.oss";
    public static final String KEY_OSS_CONFIG_KEY = "wlw.oss.config.key";

    @ApiModelProperty("配置组")
    private String group;
    @ApiModelProperty("配置组下的key")
    private String key;
    @ApiModelProperty("配置的名字")
    private String name;
    @ApiModelProperty("配置key对应的值")
    private String value;
    @ApiModelProperty("单位")
    private String unit;

}
