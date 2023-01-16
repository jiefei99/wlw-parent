package com.jike.wlw.service.product.topic;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: TopicCreateRq
 * @Author RS
 * @Date: 2023/1/14 15:51
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("注册请求参数")
public class TopicCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354115L;

    @ApiModelProperty("操作权限")
    private Operation operation;
    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("自定义类目名称")
    private String topicShortName;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("描述")
    private String desc;
}


