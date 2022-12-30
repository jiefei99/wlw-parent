package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PPhysicalModel extends PEntity implements JdbcEntity {
    private static final long serialVersionUID = -6795778097141139297L;

    public static final String TABLE_NAME = "wlw_physical_model";

    @ApiModelProperty("物模型编号")
    private String id;
    @ApiModelProperty("物模型名称")
    private String name;
    @ApiModelProperty("功能编号集合json字符串")
    private String functionIdsJson;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
