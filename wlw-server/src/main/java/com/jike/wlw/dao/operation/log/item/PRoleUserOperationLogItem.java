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
 * 2023/2/23 14:51- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("角色用户操作日志明细")
public class PRoleUserOperationLogItem extends PEntity implements JdbcEntity {
    private static final long serialVersionUID = -7763953766327557098L;

    public static final String TABLE_NAME = "wlw_operation_log_item_role_user";

    @ApiModelProperty("操作日志ID")
    private String operationLogId;
    @ApiModelProperty("被操作用户ID")
    private String userId;
    @ApiModelProperty("备注")
    private String remake;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
