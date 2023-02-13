package com.jike.wlw.service.source;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("资源信息查询条件")
public class SourceFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -4931433135204298027L;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("租户编号等于")
    private String tenantIdEq;
    @ApiModelProperty("uuid等于")
    private String uuidEq;
    @ApiModelProperty("名称等于")
    private String nameEq;
    @ApiModelProperty("环境等于")
    private SourceEvns environmentEq;
    @ApiModelProperty("类型等于")
    private SourceTypes typeEq;
    @ApiModelProperty("状态等于")
    private SourceStatus statusEq;
    @ApiModelProperty("删除等于")
    private Boolean deletedEq;

    @ApiModelProperty("uuid在之中")
    private List<String> uuidIn;
    @ApiModelProperty("名称在之中")
    private List<String> nameIn;
    @ApiModelProperty("环境在之中")
    private List<String> environmentIn;
    @ApiModelProperty("类型在之中")
    private List<String> typeIn;
    @ApiModelProperty("状态在之中")
    private List<String> statusIn;

}
