package com.jike.wlw.service.product;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: ProductQueryRq
 * @Author RS
 * @Date: 2023/1/11 19:18
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("产品查询请求参数")
public class ProductQueryRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354117L;


    @ApiModelProperty("id")
    private List<String> idIn;
    @ApiModelProperty("name")
    private String nameEq;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("显示数量")   //最大200
    private int pageSize;
    @ApiModelProperty("当前页")    //起始1
    private int currentPage;
    @ApiModelProperty("资源组ID")
    private String resourceGroupId;
    @ApiModelProperty("产品类型")
    private String aliyunCommodityCode;
}


