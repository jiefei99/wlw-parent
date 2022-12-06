package com.jike.wlw.service.author.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("用户token请求参数")
public class UserTokenCreateRq implements Serializable {
    private static final long serialVersionUID = -8062979871482876511L;

    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("用户编号")
    private String userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("用户手机号")
    private String userMobile;


}
