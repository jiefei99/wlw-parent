package com.jike.wlw.dao.config;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PConfig extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -7806253860329783277L;

    public static final String TABLE_NAME = "bsc_config";

    @ApiModelProperty("配置组")
    private String configGroup;
    @ApiModelProperty("配置组下的key")
    private String configKey;
    @ApiModelProperty("配置的名字")
    private String configName;
    @ApiModelProperty("配置key对应的值")
    private String configValue;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("租户")
    private String tenant;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
