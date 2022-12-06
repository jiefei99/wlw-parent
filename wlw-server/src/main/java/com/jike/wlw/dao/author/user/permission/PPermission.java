package com.jike.wlw.dao.author.user.permission;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("权限")
public class PPermission extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -6805343522924497143L;

    public static final String TABLE_NAME = "wlw_permission";

    @ApiModelProperty("应用ID，如：wxa、web...")
    private String appId;
    @ApiModelProperty("权限ID")
    private String id;
    @ApiModelProperty("分组id")
    private String groupId;
    @ApiModelProperty("名称")
    private String name;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
