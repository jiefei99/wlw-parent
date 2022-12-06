/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/15 12:09 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web.controller.author.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户登录结果
 */
@Getter
@Setter
@ApiModel("用户登录结果")
public class UserLoginResult<T> implements Serializable {
    private static final long serialVersionUID = -2981072820270731851L;

    @ApiModelProperty("TOKEN")
    private String token;
    @ApiModelProperty("用户对象")
    private T user;

}