package com.jike.wlw.service.author.user.credentials.account.pwd;

import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("登录账户修改")
public class PwdAccountLoginIdModifyRq implements Serializable {
    private static final long serialVersionUID = 1903818894867903683L;

    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("新账号")
    private String newLoginId;
}
