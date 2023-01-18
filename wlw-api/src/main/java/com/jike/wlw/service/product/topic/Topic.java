package com.jike.wlw.service.product.topic;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: Topic
 * @Author RS
 * @Date: 2023/1/14 15:43
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("Topic通信")
public class Topic extends StandardEntity {
    private static final long serialVersionUID = 6355685891103067786L;

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("操作权限")
    private Operation operation;
    @ApiModelProperty("产品key")
    private String productKey;
    //显示格式 ： /productKey/产品（DeviceName）/user（自定义特有）/ topicShortName
    @ApiModelProperty("自定义类目名称")
    private String topicShortName;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("描述")
    private String desc;
}


