/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/29 23:51 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials;

/**
 * 授权登录终端
 */
public enum LoginClient {

    JS("公众号"), QY("企业微信"), WEB("WEB"), APP("APP"), WXA("小程序");

    private String caption;

    private LoginClient(String caption) {
        this.caption = caption;
    }
}