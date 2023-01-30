package com.jike.wlw.dao.management.access;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PAccessRecord extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = 1401283934865992650L;

    public static final String TABLE_NAME = "wlw_access_record";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("ip")
    private String ip;
    @ApiModelProperty("浏览记录状态")
    private String status;
    @ApiModelProperty("动作")
    private String action;
    @ApiModelProperty("登陆的用户ID")
    private String loginUserId;
    @ApiModelProperty("登陆的用户名")
    private String loginUserName;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
