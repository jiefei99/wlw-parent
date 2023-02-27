package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.DirectionType;
import com.jike.wlw.service.physicalmodel.EventType;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelFunction
 * @Author RS
 * @Date: 2023/2/27 11:22
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelFunction extends Entity {

    private static final long serialVersionUID = 130322429721547201L;

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("物模型Id")
    private String modelModuleId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("功能类型")
    private ThingModelJsonType type;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否必填 （阿里数据结构中的字段）")
    private boolean required;
    @ApiModelProperty("从属id")
    private String parentId;
    @ApiModelProperty("数组大小")
    private int arraySize;
    @ApiModelProperty("数组类型")
    private DataType arrayType;
    @ApiModelProperty("是否删除")
    private int isDeleted;
    //属性
    @ApiModelProperty("读写类型")
    private String rwFlag;
    @ApiModelProperty("数据类型")
    private DataType dataType;

    //事件
    @ApiModelProperty("事件类型")
    private EventType eventType;

    //服务
    @ApiModelProperty("方法")
    private String method;
    @ApiModelProperty("服务调用类型")
    private CallType callType;

    @ApiModelProperty("输入输出参数类型")
    private DirectionType direction;
}


