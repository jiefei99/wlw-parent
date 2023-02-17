package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: PhysicalModelGetRq
 * @Author RS
 * @Date: 2023/2/15 17:27
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class PhysicalModelGetRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1298859554276354115L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("资源组ID")
    private String resourceGroupId;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("物模型版本号")
    private String modelVersion;

}


