package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PPhysicalModel extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -6795778097141139297L;

    public static final String TABLE_NAME = "wlw_physical_model";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("模块标识符")
    private String modelIdentifier = "default";
    @ApiModelProperty("模块名称")
    private String modelName = "默认模块";
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("是否发布")
    private boolean published;
    @ApiModelProperty("版本")
    private String version;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否删除")
    private boolean deleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
