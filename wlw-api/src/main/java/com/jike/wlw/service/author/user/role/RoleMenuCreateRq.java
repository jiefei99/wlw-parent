package com.jike.wlw.service.author.user.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel("新增角色权限菜单请求参数")
public class RoleMenuCreateRq implements Serializable {
    private static final long serialVersionUID = -2531762683979161111L;

    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("角色权限列表")
    private List<RoleMenu> roleMenuList;
}
