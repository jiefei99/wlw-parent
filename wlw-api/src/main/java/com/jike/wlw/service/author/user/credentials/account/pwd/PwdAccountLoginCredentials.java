/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 14:35 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials.account.pwd;


import com.jike.wlw.service.author.user.UserType;
import com.jike.wlw.service.author.user.credentials.LoginClient;
import com.jike.wlw.service.author.user.credentials.LoginCredentials;
import com.jike.wlw.service.author.user.credentials.LoginMode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 密码账号登录凭证
 */
@Getter
@Setter
@ApiModel("密码账号登录凭证")
public class PwdAccountLoginCredentials extends LoginCredentials {
    private static final long serialVersionUID = -630793132295084163L;

    @ApiModelProperty("登录账号")
    private String loginId;
    @ApiModelProperty("登录密码")
    private String password;


    public PwdAccountLoginCredentials() {
        this.setLoginMode(LoginMode.PWD);
    }

    public PwdAccountLoginCredentials(LoginClient loginClient, UserType userType, String loginId, String password) {
        this.setLoginMode(LoginMode.PWD);
        this.setLoginClient(loginClient);
        this.setUserType(userType);
        this.loginId = loginId;
        this.password = password;
    }
}