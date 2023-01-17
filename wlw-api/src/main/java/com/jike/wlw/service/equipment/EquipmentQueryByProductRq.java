package com.jike.wlw.service.equipment;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("设备查询请求参数")
public class EquipmentQueryByProductRq extends Entity implements Serializable {

    private static final long serialVersionUID = 4662576566232938440L;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("产品的ProductKey")
    public String productKey;
    @ApiModelProperty("当前页")//默认1
    public Integer currentPage;
    @ApiModelProperty("最大记录数量")//最大50 默认10
    public Integer pageSize;
    @ApiModelProperty("下一页标识")//下一页标识，首次查询无需传入。后续查询需使用的NextToken，要从上一次查询的返回结果中获取。当PageSize×CurrentPage值大于10,000时，必须传入NextToken。下一页标识，首次查询无需传入。后续查询需使用的NextToken，要从上一次查询的返回结果中获取。当PageSize×CurrentPage值大于10,000时，必须传入NextToken
    public String nextToken;
}
