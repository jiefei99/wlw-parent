package com.jike.wlw.service.product.info;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("产品SKU查询条件")
public class ProductFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -4833258583980569721L;
    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("产品key等于")
    private String productKeyEq;
    @ApiModelProperty("状态等于")
    private String statusEq;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("资源组ID")
    private String resourceGroupId;
    @ApiModelProperty("产品类型")
    private String aliyunCommodityCode;

    @ApiModelProperty("产品key等于")
    private List<String> productKeyIn;
//    @ApiModelProperty("编号在之中")
//    private List<String> idIn;
//    @ApiModelProperty("产品secret等于")
//    private String productSecretEq;
//    @ApiModelProperty("名称等于")
//    private String nameEq;
}
