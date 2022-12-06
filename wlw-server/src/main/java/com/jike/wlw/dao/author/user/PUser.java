package com.jike.wlw.dao.author.user;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("用户P对象")
public class PUser extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -7983593817975515271L;

    public static final String TABLE_NAME = "wlw_user";

    @ApiModelProperty("用户类型")
    private String userType;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("头像")
    private String headImage;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("冻结状态")
    private String status;
    @ApiModelProperty("备注")
    private String remark;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
