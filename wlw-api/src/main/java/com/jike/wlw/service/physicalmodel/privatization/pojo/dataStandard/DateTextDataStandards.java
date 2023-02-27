package com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: DateTextDataStandards
 * @Author RS
 * @Date: 2023/2/20 11:50
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class DateTextDataStandards extends Entity {
    private static final long serialVersionUID = 130322029721547643L;
    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("类型")
    private DataType dataType;
    @ApiModelProperty("数据长度")
    private Long length;
}


