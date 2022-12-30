package com.jike.wlw.service.equipment;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
public class EquipmentModifyRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("设备编号")
    private String id;
    @ApiModelProperty("设备名称")
    private String name;
    @ApiModelProperty("状态")
    private EquipmentStatus status;
    @ApiModelProperty("MQTT连接参数")
    private MQTTConnection connectMQTT;

}
