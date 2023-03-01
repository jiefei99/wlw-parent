/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 *
 * 项目名：	mark-web
 * 文件名：	PlatformType.java
 * 模块说明：
 * 修改历史：
 * 2019年4月19日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.login;

/**
 * 第三方授权登录平台类型
 *
 * @author chenpeisi
 *
 */
public enum LoginMode {

  account("账号"), wechat("微信"), qq("QQ"), alipay("支付宝"), weibo("微博");

  private String caption;

  private LoginMode(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
