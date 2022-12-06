/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 21:43 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.role;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色菜单
 */
@Getter
@Setter
@ApiModel("角色菜单")
public class RoleMenu extends Entity {
    private static final long serialVersionUID = -8047720049769254468L;

    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("权限ID")
    private String permissionId;
    @ApiModelProperty("应用ID")
    private String appId;
}