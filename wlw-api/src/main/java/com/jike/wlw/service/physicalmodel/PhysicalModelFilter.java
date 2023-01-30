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
    @ApiModelProperty("编号等于")
    private String idEq;
    @ApiModelProperty("名称等于")
    private String nameEq;

    @ApiModelProperty("编号在之中")
    private List<String> idIn;

}
