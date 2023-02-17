package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: PhysicalModelCopyRq
 * @Author RS
 * @Date: 2023/2/15 17:14
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class PhysicalModelCopyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -2698859554276354115L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("复制的物模型所属产品的ProductKey")
    private String sourceProductKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("目标产品的ProductKey")
    private String targetProductKey;
    @ApiModelProperty("资源组ID")
    private String resourceGroupId;
    @ApiModelProperty("复制的物模型版本号")
    private String sourceModelVersion;
}


