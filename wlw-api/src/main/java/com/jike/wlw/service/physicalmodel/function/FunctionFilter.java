package com.jike.wlw.service.physicalmodel.function;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("功能查询条件")
public class FunctionFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 9214479469460671259L;

    @ApiModelProperty("编号等于")
    private String tenantIdEq;
    @ApiModelProperty("编号等于")
    private String idEq;
    @ApiModelProperty("名称等于")
    private String nameEq;
    @ApiModelProperty("类型等于")
    private FunctionType typeEq;
    @ApiModelProperty("读写类型等于")
    private AccessMode accessModeEq;
    @ApiModelProperty("是否标准功能的标准属性")
    private Boolean requiredEq;


    @ApiModelProperty("编号在之中")
    private List<String> idIn;


}
