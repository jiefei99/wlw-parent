package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: PhysicalModelPubGetRq
 * @Author RS
 * @Date: 2023/2/15 17:58
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelPubTslGetRq extends Entity implements Serializable {
    private static final long serialVersionUID = -2398859554276354115L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("设备所属的产品ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("物模型自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("物模型版本号")
    private String modelVersion;
    @ApiModelProperty("资源组")
    private String resourceGroupId;
    @ApiModelProperty("是否获取精简版")
    private boolean Simple;
}


