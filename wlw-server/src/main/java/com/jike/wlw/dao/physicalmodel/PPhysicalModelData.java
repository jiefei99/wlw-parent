package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PPhysicalModelData
 * @Author RS
 * @Date: 2023/2/16 17:38
 * @Version 1.0
 */

@Getter
@Setter
public class PPhysicalModelData  extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -6795778097141439297L;

    public static final String TABLE_NAME = "wlw_model_device_data";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("父类Id")
    private String belongToId;
    @ApiModelProperty("数据")
    private String data;
    @ApiModelProperty("数据范围")
    private String dataRange;
    @ApiModelProperty("逻辑删除")
    private boolean isDeleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


