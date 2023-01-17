package com.jike.wlw.service.equipment;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("设备统计数据请求参数")
public class EquipmentStatisticsQueryRq extends Entity implements Serializable {

    private static final long serialVersionUID = -4174754218136925915L;
    @ApiModelProperty("设备名称")
    public String deviceName;
    @ApiModelProperty("设备ID")
    public String iotId;
    @ApiModelProperty("实例ID")
    public String GroupId;
    @ApiModelProperty("所属产品的ProductKey")
    public String productKey;
}
