package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PPhysicalModelModule extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -6795778097141139297L;

    public static final String TABLE_NAME = "wlw_physical_model_module";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("模块标识符")
    private String identifier;
    @ApiModelProperty("模块名称")
    private String name;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("是否发布")
    private boolean published;
    @ApiModelProperty("版本")
    private String version;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否删除")
    private int isDeleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
