package com.jike.wlw.service.product.topic;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: TopicModifyRq
 * @Author RS
 * @Date: 2023/1/14 15:53
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("修改请求参数")
public class TopicModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -2698859554276354115L;

    @ApiModelProperty("操作权限")
    private Operation operation;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("自定义类目名称")
    private String topicShortName;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("描述")
    private String desc;

}


