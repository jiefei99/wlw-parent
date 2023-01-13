package com.jike.wlw.core.product.ali.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ConsumerGroupRq
 * @Author RS
 * @Date: 2023/1/7 17:59
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("消费组请求参数")
public class ConsumerGroupRq implements Serializable {
    private static final long serialVersionUID = 7451111224272538946L;

    @ApiModelProperty("该订阅中的产品的ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("消费组名称")
    private String groupName;
    @ApiModelProperty("消费组ID")
    private String groupId;
    @ApiModelProperty("指定显示返回结果中的第几页,最小值为1")
    private Integer currentPage;
    @ApiModelProperty("指定返回结果中每页显示的消费组数量，最小值为1，最大值为1000")
    private Integer pageSize;
    @ApiModelProperty("是否使用模糊查询")
    private boolean fuzzy=false;
}


