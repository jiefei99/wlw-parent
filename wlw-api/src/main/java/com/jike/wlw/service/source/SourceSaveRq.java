package com.jike.wlw.service.source;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("资源信息")
public class SourceSaveRq implements Serializable {
    private static final long serialVersionUID = 6220488135956881319L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("环境")
    private SourceEvns environment;
    @ApiModelProperty("类型")
    private SourceTypes type;

    @ApiModelProperty("资源连接参数")
    private SourceInfo sourceInfo;
}
