package com.jike.wlw.service.operation.log;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.operation.log.item.RolePermissionMenuOperationLogItem;
import com.jike.wlw.service.operation.log.item.RoleUserOperationLogItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/6
 * @apiNote
 */
@Setter
@Getter
@ApiModel("操作日志")
public class OperationLog extends StandardEntity {
    private static final long serialVersionUID = -8724602730363820695L;

    /* 信息块：角色权限菜单操作日志明细 */
    public static final String PART_ROLE_PERMISSION_MENU_ITEM = "role.permission.menu.item";
    /* 信息块：角色用户操作日志明细 */
    public static final String PART_ROLE_USER_ITEM = "role.user.item";
    /* 信息块：员工信息 */
    public static final String PART_EMPLOYEE = "employee";
    /* 信息块：角色信息 */
    public static final String PART_ROLE = "role";

    @ApiModelProperty("操作类型")
    private OperationType type;
    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("关联ID。eg：商品ID、订单ID")
    private String relationId;
    @ApiModelProperty("操作内容")
    private String content;
    @ApiModelProperty("备注")
    private String remake;

    // 辅助属性
    @ApiModelProperty("角色")
    private Role role;
    @ApiModelProperty("操作人（员工）")
    private Employee employee;

    @ApiModelProperty("角色权限菜单操作明细集合")
    private List<RolePermissionMenuOperationLogItem> rolePermissionMenuOperationLogItemList;
    @ApiModelProperty("（旧）角色权限菜单")
    private String oldRolePermissionMenu;
    @ApiModelProperty("（新）角色权限菜单")
    private String newRolePermissionMenu;

    @ApiModelProperty("角色用户操作明细集合")
    private List<RoleUserOperationLogItem> roleUserOperationLogItemList;
    @ApiModelProperty("角色用户操作信息")
    private String roleUserOperationInfo;

}
