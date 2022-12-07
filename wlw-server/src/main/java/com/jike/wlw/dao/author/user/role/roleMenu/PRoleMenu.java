package com.jike.wlw.dao.author.user.role.roleMenu;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/9 20:10- sufengjia - 创建。
 */
@Getter
@Setter
public class PRoleMenu extends PEntity implements JdbcEntity {
    private static final long serialVersionUID = 2618630595083527504L;

    public static final String TABLE_NAME = "wlw_role_menu";

    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("权限ID")
    private String permissionId;
    @ApiModelProperty("应用ID")
    private String appId;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
