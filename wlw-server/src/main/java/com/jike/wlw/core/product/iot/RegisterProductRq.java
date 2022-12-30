package com.jike.wlw.core.product.iot;

import com.jike.wlw.service.product.ProtocolType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("注册阿里产品请求参数")
public class RegisterProductRq implements Serializable {
    private static final long serialVersionUID = 7456101024072538946L;

    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("节点类型")   //0:设备    1:网关
    private Integer nodeType;
    @ApiModelProperty("数据格式")   //0:透传/自定义格式（CUSTOM_FORMAT）    1:Alink协议（ALINK_FORMAT）
    private Integer dataFormat;
    @ApiModelProperty("设备接入网关的协议类型") //customize自定义协议
    private ProtocolType protocolType;
    @ApiModelProperty("产品描述")
    private String description;


}
