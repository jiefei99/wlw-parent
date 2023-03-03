package com.jike.wlw.service.operation.log.item;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.author.auth.RolePermissionMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/23 14:27- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("角色权限菜单操作日志明细")
public class RolePermissionMenuOperationLogItem extends Entity {
    private static final long serialVersionUID = 5009279986689140598L;

    @ApiModelProperty("操作日志ID")
    private String operationLogId;
    @ApiModelProperty("（旧）角色权限菜单集合")
    private List<RolePermissionMenu> rolePermissionMenuList;
    @ApiModelProperty("备注")
    private String remake;

}
