package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: PhysicalModelServiceFilter
 * @Author RS
 * @Date: 2023/2/16 20:04
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("物模型功能查询条件")
public class PhysicalModelFunctionFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 9214479469461671259L;
    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("模块等于")
    private String modelModuleIdEq;
    @ApiModelProperty("标识符等于")
    private List<String> identifierIn;
    @ApiModelProperty("功能类型")
    private ThingModelJsonType type;
}


