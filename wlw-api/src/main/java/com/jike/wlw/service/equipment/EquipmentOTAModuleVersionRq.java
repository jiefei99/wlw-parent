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
public class EquipmentOTAModuleVersionRq extends Entity implements Serializable {
    private static final long serialVersionUID = -2300813288035855450L;

    @ApiModelProperty("设备ID")
    public String id;
    @ApiModelProperty("设备密钥")
    public String name;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("当前页")
    public Integer currentPage = 1;
    @ApiModelProperty("最大记录数量")
    public Integer pageSize = 10;
}
