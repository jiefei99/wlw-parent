/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年02月26日 23:16 - ASUS - 创建。
 */
package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
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
