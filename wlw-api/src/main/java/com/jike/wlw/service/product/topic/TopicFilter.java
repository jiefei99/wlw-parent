package com.jike.wlw.service.product.topic;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: TopicFilter
 * @Author RS
 * @Date: 2023/1/14 15:55
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("查询请求参数")
public class TopicFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -3698859554276354115L;

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("操作权限")
    private String operation;
}


