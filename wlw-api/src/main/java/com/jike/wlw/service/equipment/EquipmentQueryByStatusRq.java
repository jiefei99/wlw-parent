package com.jike.wlw.service.equipment;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("根据状态查询设备列表请求参数")
public class EquipmentQueryByStatusRq extends Entity implements Serializable {

    private static final long serialVersionUID = -7360427722419252023L;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;
    @ApiModelProperty("所属资源组ID")
    public String resourceGroupId;
    @ApiModelProperty("状态")
    public Integer status;
    @ApiModelProperty("当前页")
    public Integer currentPage;
    @ApiModelProperty("最大记录数量")
    public Integer pageSize;
}
