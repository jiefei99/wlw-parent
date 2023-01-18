package com.jike.wlw.service.serverSubscription.consumerGroup;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ConsumerGroupFilter
 * @Author RS
 * @Date: 2023/1/13 14:53
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("查询消费组请求参数")
public class ConsumerGroupFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7698859554276354121L;

//    @ApiModelProperty("当前页")
//    private int currentPage;
    @ApiModelProperty("租户")
    private String tenantId;
//    @ApiModelProperty("分页大小")
//    private int pageSize;
    @ApiModelProperty("是否使用模糊查询")
    private boolean fuzzy=false;
    @ApiModelProperty("消费组Id")
    private String idEq;
    @ApiModelProperty("消费组名称")
    private String nameLike;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
}


