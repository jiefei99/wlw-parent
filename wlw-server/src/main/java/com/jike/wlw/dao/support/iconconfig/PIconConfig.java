package com.jike.wlw.dao.support.iconconfig;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("图标配置")
public class PIconConfig extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = 8782968316124800189L;

    public static final String TABLE_NAME = "wlw_icon_config";

    @ApiModelProperty("图标链接")
    private String url;
    @ApiModelProperty("图标描述")
    private String description;
    @ApiModelProperty("应用类型")
    private String appId;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
