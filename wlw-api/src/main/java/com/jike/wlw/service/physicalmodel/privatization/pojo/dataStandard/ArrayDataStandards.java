package com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: ArrayDataStandards
 * @Author RS
 * @Date: 2023/2/20 11:57
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class ArrayDataStandards extends Entity {
    private static final long serialVersionUID = 130322029741547643L;

    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("类型")
    private DataType dataType;
    @ApiModelProperty("长度")
    private Long size;
    @ApiModelProperty("元素的数据类型")
    private DataType childDataType;
    @ApiModelProperty("数据规范")
    private String dataSpecs;
    @ApiModelProperty("数据规范")
    private List<String> dataSpecsList;
}


