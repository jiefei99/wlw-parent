package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: PhysicalModelDelRq
 * @Author RS
 * @Date: 2023/2/15 17:17
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelDelRq extends Entity implements Serializable {
    private static final long serialVersionUID = -2618859554236354115L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("资源组ID")
    private String resourceGroupId;
    @ApiModelProperty("标识符")
    private String functionBlockId;
    @ApiModelProperty("属性标识符")
    private List<String> propertyIdentifier;
    @ApiModelProperty("服务标识符")
    private List<String> serviceIdentifier;
    @ApiModelProperty("事件标识符")
    private List<String> eventIdentifier;
}


