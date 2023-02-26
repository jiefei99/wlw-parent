package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @title: EnumBOOLDataStandards
 * @Author RS
 * @Date: 2023/2/20 12:03
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class EnumBoolDataStandards extends Entity {
    private static final long serialVersionUID = 130322229721547643L;
    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("类型")
    private String dataType;
    @ApiModelProperty("key-value")
    private Map<Integer,String> enumMap;
}


