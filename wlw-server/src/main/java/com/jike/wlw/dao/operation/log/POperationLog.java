package com.jike.wlw.dao.operation.log;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * @author mengchen
 * @date 2022/7/6
 * @apiNote
 */
@Setter
@Getter
public class POperationLog extends PStandardEntity implements JdbcEntity  {

    private static final long serialVersionUID = 9125217067940776328L;

    public static final String TABLE_NAME = "eguard_operation_log";

    @ApiModelProperty("操作类型")
    private String type;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("关联ID")
    private String relationId;
    @ApiModelProperty("操作内容")
    private String content;
    @ApiModelProperty("操作备注")
    private String remake;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
