package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: DataStandards
 * @Author RS
 * @Date: 2023/2/20 11:56
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class DataStandards  extends Entity {
    private static final long serialVersionUID = 130322029721527603L;

    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("类型")
    private String dataType;
}


