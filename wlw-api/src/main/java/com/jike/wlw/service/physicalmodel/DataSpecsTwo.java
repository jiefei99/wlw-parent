package com.jike.wlw.service.physicalmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/3/17
 */
@Getter
@Setter
@ApiModel
public class DataSpecsTwo implements Serializable {
    private static final long serialVersionUID = 1800112445891755898L;

    @ApiModelProperty("备注")
    private String dataType;
    @ApiModelProperty("备注")
    private String name;
    //dataType为ENUM
    @ApiModelProperty("备注")
    private Boolean custom;
    @ApiModelProperty("备注")
    private String defaultValue;
    @ApiModelProperty("备注")
    private Integer value;

    //dataType为STRUCT
    @ApiModelProperty("备注")
    private String childDataType;
    @ApiModelProperty("备注")
    private String childName;
    @ApiModelProperty("备注")
    private String identifier;
    @ApiModelProperty("备注")
    private DataSpecs dataSpecs;

}
