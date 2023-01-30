package com.jike.wlw.service.product.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: AliProductFilter
 * @Author RS
 * @Date: 2023/1/30 10:37
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("阿里产品SKU查询条件")
public class AliProductFilter {
    @ApiModelProperty("产品类型")
    private String aliyunCommodityCode;
    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("实例")
    private String iotInstanceId;
    @ApiModelProperty("显示数量")
    private Integer pageSize;
    @ApiModelProperty("资源组ID")
    private String resourceGroupId;
}


