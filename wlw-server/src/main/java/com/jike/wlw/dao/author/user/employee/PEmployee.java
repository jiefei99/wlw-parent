/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/23 15:51 - chenpeisi - 创建。
 */
package com.jike.wlw.dao.author.user.employee;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 员工
 */
@Getter
@Setter
@ApiModel("员工")
public class PEmployee extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = 427770589118873230L;

    public static final String TABLE_NAME = "wlw_employee";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("ID")
    private String id;
    @ApiModelProperty("是否为管理员")
    private Boolean admin = false;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("备注")
    private String remark;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}