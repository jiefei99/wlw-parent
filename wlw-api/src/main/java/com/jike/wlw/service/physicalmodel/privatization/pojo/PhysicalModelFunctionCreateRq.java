package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelEvent;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelProperties;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: PhysicalModelFunctionCreateRq
 * @Author RS
 * @Date: 2023/2/17 14:18
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelFunctionCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354115L;

    @ApiModelProperty("物模型模块Id")
    private String modelModuleId;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("属性")
    private List<ModelProperties> properties;
    @ApiModelProperty("服务")
    private List<ModelService> services;
    @ApiModelProperty("事件")
    private List<ModelEvent> events;
}


