package com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: NumberDataStandards
 * @Author RS
 * @Date: 2023/2/20 11:45
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class NumberDataStandards extends Entity {
    private static final long serialVersionUID = 130322029721547603L;
    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("类型")
    private DataType dataType;
    @ApiModelProperty("最大值")
    private String max;
    @ApiModelProperty("最小值")
    private String min;
    @ApiModelProperty("步长")
    private String step;
    @ApiModelProperty("精度")
    private String precise;
    @ApiModelProperty("单位符号")
    private String unit;
    @ApiModelProperty("单位名称")
    private String unitName;
}


