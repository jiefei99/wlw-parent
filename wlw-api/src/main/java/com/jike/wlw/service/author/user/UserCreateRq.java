/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/23 13:41 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("新增用户请求参数")
public class UserCreateRq implements Serializable {
    private static final long serialVersionUID = -1864509945078913882L;

    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("头像")
    private String headImage;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("备注")
    private String remark;

}