package com.jike.wlw.sys.web.controller.subscription.subscribe;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("服务端订阅请求参数")
public class SubscribeRq extends Entity {
    private static final long serialVersionUID = 4486844823528292407L;

    @ApiModelProperty("产品Key")
    private String productKey;
    @ApiModelProperty("订阅类型")
    private String type;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;

}
