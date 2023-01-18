package com.jike.wlw.dao.serverSubscription.subscribe;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PSubscribe
 * @Author RS
 * @Date: 2023/1/16 14:15
 * @Version 1.0
 */

@Getter
@Setter
public class PSubscribe extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264769L;

    public static final String TABLE_NAME = "wlw_subscribe";

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("消费组Id")
    private String consumerGroupIds;
    @ApiModelProperty("MNS队列的配置信息")
    private String mnsConfiguration;
    @ApiModelProperty("当前订阅产品的其他类型消息")
    private String subscribeFlags;
    @ApiModelProperty("订阅类型")
    private String type;
    @ApiModelProperty("推送消息类型")
    private String pushMessageType;
    @ApiModelProperty("逻辑删除")
    private int isDeleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


