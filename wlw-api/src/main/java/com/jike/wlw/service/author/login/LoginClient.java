/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 *
 * 项目名：	mark-web
 * 文件名：	ClientType.java
 * 模块说明：
 * 修改历史：
 * 2019年4月19日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.login;

import java.io.Serializable;

/**
 *
 * 授权登录终端
 *
 * @author chenpeisi
 *
 */
public enum LoginClient implements Serializable {

  js("公众号"), web("web"), app("APP"), wxa("小程序");

  private String caption;

  private LoginClient(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
