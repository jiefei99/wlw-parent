package com.jike.wlw.service.product;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel
public class ProductCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("节点类型")   //0:设备    1:网关
    private Integer nodeType;
    @ApiModelProperty("数据格式")   //0:透传/自定义格式（CUSTOM_FORMAT）    1:Alink协议（ALINK_FORMAT）
    private Integer dataFormat;
    @ApiModelProperty("设备接入网关的协议类型") //customize自定义协议
    private ProtocolType protocolType;
    @ApiModelProperty("物模型编号集合")
    private List<String> physicalModelIds;

    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("备注")
    private String remark;

}
