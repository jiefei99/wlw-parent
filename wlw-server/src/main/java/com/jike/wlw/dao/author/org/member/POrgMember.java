package com.jike.wlw.dao.author.org.member;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Getter
@Setter
@ApiModel("组织成员")
public class POrgMember extends PStandardEntity implements JdbcEntity {

    private static final long serialVersionUID = 30285540347902813L;

    public static final String TABLE_NAME = "wlw_org_member";

    @ApiModelProperty("组织ID")
    private String orgId;
    @ApiModelProperty("成员头像")
    private String image;
    @ApiModelProperty("组织类型")
    private String orgType;
    @ApiModelProperty("成员工号")
    private String number;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("是否是管理员。true：是")
    private Boolean isAdmin;
    @ApiModelProperty("组织启用状态")
    private String status;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("自动生成标示")
    private Integer id;


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
