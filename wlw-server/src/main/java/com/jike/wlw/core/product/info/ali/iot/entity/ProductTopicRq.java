package com.jike.wlw.core.product.info.ali.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ProductTopicRq
 * @Author RS
 * @Date: 2023/1/7 17:48
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("产品自定义Topic请求参数")
public class ProductTopicRq implements Serializable {
    private static final long serialVersionUID = 7451111024272538946L;

    @ApiModelProperty("操作权限")
    private String operation;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("Topic类的自定义类目名称")
    private String topicShortName;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("Topic类的ID")
    private String topicId;
    @ApiModelProperty("Topic类的描述信息")
    private String desc;
}


