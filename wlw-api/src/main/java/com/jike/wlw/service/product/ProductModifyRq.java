package com.jike.wlw.service.product;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class ProductModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("产品密钥") 
    private String productKey;
    @ApiModelProperty("名称") //阿里支持同步修改
    private String name;
    @ApiModelProperty("描述") //阿里支持同步修改
    private String description;
    @ApiModelProperty("备注")
    private String remark;

}
