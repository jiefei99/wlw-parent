package com.jike.wlw.dao.operation.log.item;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/23 14:44- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("角色权限菜单操作日志明细P对象")
public class PRolePermissionMenuOperationLogItem extends PEntity implements JdbcEntity {
    private static final long serialVersionUID = 7573888371651062717L;

    public static final String TABLE_NAME = "eguard_operation_log_item_role_permission_menu";

    @ApiModelProperty("操作日志ID")
    private String operationLogId;
    @ApiModelProperty("（旧）角色权限菜单集合Json")
    private String oldRolePermissionMenuListJson;
    @ApiModelProperty("（新）角色权限菜单集合Json")
    private String newRolePermissionMenuListJson;
    @ApiModelProperty("备注")
    private String remake;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
