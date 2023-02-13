package com.jike.wlw.service.source;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("资源")
public class Source extends StandardEntity {
    private static final long serialVersionUID = 664293227949105584L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("环境")
    private SourceEvns environment;
    @ApiModelProperty("类型")
    private SourceTypes type;
    @ApiModelProperty("是否删除")
    private Boolean deleted;
    @ApiModelProperty("连接状态")
    private SourceStatus status;
    @ApiModelProperty("资源连接参数")
    private SourceInfo sourceInfo;
}
