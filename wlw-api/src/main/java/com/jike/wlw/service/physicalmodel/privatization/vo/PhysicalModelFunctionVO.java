/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年02月27日 0:25 - ASUS - 创建。
 */
package com.jike.wlw.service.physicalmodel.privatization.vo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.EventType;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelFunctionVO extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554276354145L;

    @ApiModelProperty("物模型Id")
    private String modelModuleId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("功能类型")
    private ThingModelJsonType type;
    @ApiModelProperty("数据类型")
    private DataType dataType;
    @ApiModelProperty("事件类型")
    private EventType eventType;
    @ApiModelProperty("服务调用类型")
    private CallType callType;
}
