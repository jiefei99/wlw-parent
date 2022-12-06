/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/15 10:33 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web.sso;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * 应用系统常量
 */
@ApiModel("应用系统常量")
public class AppConstant implements Serializable {
    private static final long serialVersionUID = 8829266193699256258L;

    public static final String APP_USER_TYPE = "userType";
    public static final String APP_USER_ID = "userId";
    public static final String APP_USER_NAME = "userName";
    public static final String APP_MOBILE = "mobile";
    public static final String APP_TENANT = "tenant";
}