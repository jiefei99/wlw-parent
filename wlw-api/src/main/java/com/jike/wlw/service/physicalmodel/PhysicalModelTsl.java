package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelTsl
 * @Author RS
 * @Date: 2023/2/15 22:49
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelTsl extends StandardEntity {
    private static final long serialVersionUID = 1102343985736867036L;
    @ApiModelProperty("str")
    private String tslStr;
    @ApiModelProperty("uri")
    private String tslUri;
}


