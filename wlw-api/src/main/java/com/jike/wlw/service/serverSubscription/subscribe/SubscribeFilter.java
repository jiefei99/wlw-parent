package com.jike.wlw.service.serverSubscription.subscribe;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: SubscribeFilter
 * @Author RS
 * @Date: 2023/1/16 15:08
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("查询订阅请求参数")
public class SubscribeFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7888859554276354121L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("订阅类型")
    private String type;
    @ApiModelProperty("消费组Id")
    private String groupIdEq;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("productKey")
    private String productKey;
}


