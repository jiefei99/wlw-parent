/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年02月26日 23:16 - ASUS - 创建。
 */
package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
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
 *
 *
 * @author ASUS
 * @since 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelFunctionModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354145L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("物模型模块表示符号")
    private String moduleIdentifier;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("物模型模块Id")
    private String modelModuleId;
    @ApiModelProperty("属性")
    private List<ModelProperties> properties;
    @ApiModelProperty("服务")
    private List<ModelService> services;
    @ApiModelProperty("事件")
    private List<ModelEvent> events;
}
