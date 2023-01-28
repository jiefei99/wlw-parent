package com.jike.wlw.dao.serverSubscription.consumerGroup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @title: PConsumerGroup
 * @Author RS
 * @Date: 2023/1/13 15:20
 * @Version 1.0
 */

@Getter
@Setter
public class PConsumerGroup extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264763L;

    public static final String TABLE_NAME = "wlw_consumer_group";

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("消费组Id")
    private String id;
    @ApiModelProperty("消费组名称")
    private String name;
    @ApiModelProperty("逻辑删除")
    private int isDeleted;
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


