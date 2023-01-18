package com.jike.wlw.dao.product.topic;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PTopic
 * @Author RS
 * @Date: 2023/1/16 17:48
 * @Version 1.0
 */
@Getter
@Setter
public class PTopic extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264769L;

    public static final String TABLE_NAME = "wlw_topic";

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("自定义类目名称")
    private String topicShortName;
    @ApiModelProperty("操作权限")
    private String operation;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("逻辑删除")
    private int isDeleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


