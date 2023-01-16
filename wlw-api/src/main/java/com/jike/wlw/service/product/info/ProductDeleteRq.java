package com.jike.wlw.service.product.info;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: ProductDelRq
 * @Author RS
 * @Date: 2023/1/11 19:18
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class ProductDeleteRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354116L;

    @ApiModelProperty("该订阅中的产品的ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
}


