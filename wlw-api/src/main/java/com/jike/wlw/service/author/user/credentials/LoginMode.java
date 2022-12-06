/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	mark-wechatweb
 * 文件名：	PlatformType.java
 * 模块说明：
 * 修改历史：
 * 2019年4月19日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials;

/**
 * 第三方授权登录平台类型
 *
 * @author chenpeisi
 */
public enum LoginMode {

    PWD("密码"), MOBILE("手机验证码"), WECHAT("微信"), QQ("QQ"), ALIPAY("支付宝"), WEIBO("微博");

    private String caption;

    private LoginMode(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
