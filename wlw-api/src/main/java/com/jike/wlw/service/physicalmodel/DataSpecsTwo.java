package com.jike.wlw.service.physicalmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wza
 * @create 2023/3/17
 */
@Getter
@Setter
@ApiModel
public class DataSpecsTwo implements Serializable {
    private static final long serialVersionUID = 1800112445891755898L;
    //ARRAY和STRUCT类型数据相互嵌套时，最多支持递归嵌套2层（父和子）
    @ApiModelProperty("数据类型")
    private String dataType;
    @ApiModelProperty("属性名称")
    private String name;
    //dataType为ENUM
    @ApiModelProperty("是否是自定义功能")
    private Boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("枚举值")
    private Integer value;

    //dataType为STRUCT
    @ApiModelProperty("结构体中子参数的数据类型")
    private String childDataType;
    @ApiModelProperty("结构体中的子参数名称")
    private String childName;
    @ApiModelProperty("结构体中的子参数的标识符")
    private String identifier;
    @ApiModelProperty("数据规范")
    private DataSpecs dataSpecs;
    @ApiModelProperty("数据规范")
    private List<DataSpecsTwo> dataSpecsList;

}
