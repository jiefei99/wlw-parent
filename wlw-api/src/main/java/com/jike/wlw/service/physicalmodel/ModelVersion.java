package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: ModelVersion
 * @Author RS
 * @Date: 2023/2/15 19:03
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("物模型版本")
public class ModelVersion extends StandardEntity {
    private static final long serialVersionUID = 1102343985736867036L;

    @ApiModelProperty("物模型编号")
    private String modelVersion;
    @ApiModelProperty("描述")
    private String description;
}


