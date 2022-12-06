package com.jike.wlw.dao.author.org;

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
@ApiModel("组织")
public class POrg extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -4771510132804525052L;

    public static final String TABLE_NAME = "wlw_org";

    @ApiModelProperty("组织类型")
    private String orgType;
    @ApiModelProperty("上级组织ID")
    private String upperId;
    @ApiModelProperty("组织名称")
    private String name;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("自动生产标示")
    private Integer id;
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
