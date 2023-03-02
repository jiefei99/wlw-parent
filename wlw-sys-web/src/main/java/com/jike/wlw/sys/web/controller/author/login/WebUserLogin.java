/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	bigbomb-web
 * 文件名：	LoginInfo.java
 * 模块说明：
 * 修改历史：
 * 2018年7月8日 - lsz - 创建。
 */
package com.jike.wlw.sys.web.controller.author.login;

import com.jike.wlw.service.author.login.LoginClient;
import com.jike.wlw.service.author.login.LoginCredentials;
import com.jike.wlw.service.author.login.LoginMode;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lsz
 *
 */
@Getter
@Setter
@ApiModel
public class WebUserLogin implements Serializable {
    private static final long serialVersionUID = 9036474802433207830L;

    @ApiModelProperty("登录账号")
    private String loginId;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String captcha;

    public LoginCredentials buildUserCredentials() {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setLoginMode(LoginMode.account);
        credentials.setLoginClient(LoginClient.web);
        credentials.setUserType(UserType.EMPLOYEE);
        credentials.setLoginId(loginId);
        credentials.setPassword(password);
        return credentials;
    }

}
