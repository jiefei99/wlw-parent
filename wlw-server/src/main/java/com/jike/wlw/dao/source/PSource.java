package com.jike.wlw.dao.source;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wza
 * @create 2023/1/10
 */
@Getter
@Setter
public class PSource extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = 8650201454877675568L;
    public static final String TABLE_NAME = "wlw_source";

    @ApiModelProperty("租户编号")
    private String tenantId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("环境")
    private String environment;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("资源连接参数json")
    private String parameter;
    @ApiModelProperty("是否删除")
    private Boolean deleted = false;
    @ApiModelProperty("连接状态")
    private String Status;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
