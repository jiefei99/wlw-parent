package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("物模型查询条件")
public class PhysicalModelFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 9214479469460671259L;

    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("productKey")
    private String productKey;

}
