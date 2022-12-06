/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 14:37 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials.account.pwd;


import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 密码账户修改
 */
@Getter
@Setter
@ApiModel("密码账户忘记")
public class PwdAccountForgetRq implements Serializable {
    private static final long serialVersionUID = 3106243774277412358L;

    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("用户手机号")
    private String mobile;
    @ApiModelProperty("新密码")
    private String newPassword;

}