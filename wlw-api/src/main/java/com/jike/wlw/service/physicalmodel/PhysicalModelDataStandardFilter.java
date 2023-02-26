package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelDataTypeStandardFilter
 * @Author RS
 * @Date: 2023/2/16 18:04
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel
public class PhysicalModelDataStandardFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 9214479469461671259L;

    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("父类Id")
    private String parentIdEq;
}


