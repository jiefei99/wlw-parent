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
public class DataSpecs implements Serializable {
    private static final long serialVersionUID = 1800112445891755898L;

    @ApiModelProperty("备注")
    private Boolean custom;
    @ApiModelProperty("备注")
    private String dataType;
    //dataType为INT
    @ApiModelProperty("备注")
    private String defaultValue;
    @ApiModelProperty("备注")
    private String max;
    @ApiModelProperty("备注")
    private String min;

    //dataType为TEXT
    @ApiModelProperty("备注")
    private Integer id;
    @ApiModelProperty("备注")
    private Integer length;

    //dataType为ARRAY
    @ApiModelProperty("备注")
    private String childDataType;
    @ApiModelProperty("备注")
    private Boolean size;
}
