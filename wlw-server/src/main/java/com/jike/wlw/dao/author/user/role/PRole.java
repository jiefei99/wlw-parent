package com.jike.wlw.dao.author.user.role;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("角色")
public class PRole extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -9098419521435313474L;

    public static final String TABLE_NAME = "wlw_role";

    @ApiModelProperty("角色名称 ")
    private String name;
    @ApiModelProperty("备注")
    private String remark;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
