/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 16:15 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.role;

import com.jike.wlw.service.author.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户角色
 */
@Getter
@Setter
@ApiModel("用户角色")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 809720360587001489L;

    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("角色ID")
    private String roleId;

    //辅助字段
    @ApiModelProperty("用户信息")
    private User user;

}