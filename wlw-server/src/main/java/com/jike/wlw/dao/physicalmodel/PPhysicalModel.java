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

    public static final String TABLE_NAME = "wlw_model_device";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("物模型标识符")
    private String identifier;
    @ApiModelProperty("物模型名称")
    private String name;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("是否发布")
    private boolean isPublish;
    @ApiModelProperty("版本")
    private String version;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否删除")
    private boolean isDeleted;

    @ApiModelProperty("功能编号集合json字符串")
    private String functionIdsJson;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
