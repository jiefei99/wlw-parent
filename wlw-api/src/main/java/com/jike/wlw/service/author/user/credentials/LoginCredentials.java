/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	chillbaby-wechatweb
 * 文件名：	UserCredentials.java
 * 模块说明：
 * 修改历史：
 * 2018年7月8日 - lsz - 创建。
 */
package com.jike.wlw.service.author.user.credentials;


import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录凭证
 */
@Getter
@Setter
@ApiModel("登录凭证")
public class LoginCredentials implements Serializable {
    private static final long serialVersionUID = 1445981013050216355L;

    @ApiModelProperty("登录方式")
    private LoginMode loginMode;
    @ApiModelProperty("授权登录终端")
    private LoginClient loginClient;
    @ApiModelProperty("用户类型")
    private UserType userType;


}
