package com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: StructDataStandards
 * @Author RS
 * @Date: 2023/2/20 12:00
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class StructDataStandards extends Entity {
    private static final long serialVersionUID = 130322024721547603L;
    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("类型")
    private String dataType;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("子参数的数据类型")
    private String childDataType;
    @ApiModelProperty("子参数名称")
    private String childName;
    @ApiModelProperty("数据规范")
    private String dataSpecs;
    @ApiModelProperty("数据规范")
    private List<String> dataSpecsList;
}


