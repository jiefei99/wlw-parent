/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/12 15:03 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.employee;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.role.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("员工")
public class Employee extends StandardEntity {
    private static final long serialVersionUID = 8456162963968726410L;

    public static final String PARTS_USER = "user";
    public static final String PARTS_PWD_ACCOUNT = "pwdAccount";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("是否为管理员")
    private Boolean admin = false;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("loginId")
    private String loginId;
    @ApiModelProperty("账号密码")
    private String password;

    // 辅助属性
    @ApiModelProperty("用户信息")
    private User user;
    @ApiModelProperty("角色集合")
    private List<Role> roles;
}
