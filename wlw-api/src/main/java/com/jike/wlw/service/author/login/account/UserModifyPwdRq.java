package com.jike.wlw.service.author.login.account;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: UserModifyPwd
 * @Author RS
 * @Date: 2023/3/2 17:27
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("修改密码")
public class UserModifyPwdRq extends Entity {
    private static final long serialVersionUID = -7405894179191402555L;

    @ApiModelProperty("原密码")
    private String oldPassword;
    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("uuid")
    private String id;
}


