package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelBase
 * @Author RS
 * @Date: 2023/2/20 9:41
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelBase extends Entity {
    private static final long serialVersionUID = 130322029721547601L;
    @ApiModelProperty("功能名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("数据类型")
    private String dataType;
    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("是否是自定义功能")
    private String custom;
//    @ApiModelProperty("父类Id")
//    private String belongToId;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否必要")
    private boolean required;
}


