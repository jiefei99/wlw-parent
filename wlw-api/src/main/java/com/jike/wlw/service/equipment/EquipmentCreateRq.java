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
public class EquipmentCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("设备编号")
    private String id;
//    @ApiModelProperty("设备三元组deviceSecret")  //设备三元组
//    private String deviceSecret;
    @ApiModelProperty("设备三元组productKey")  //设备三元组
    private String productKey;
    @ApiModelProperty("设备名称")
    private String name;
//    @ApiModelProperty("产品编号")
//    private String productId;
    @ApiModelProperty("MQTT连接参数")
    private MQTTConnection connectMQTT;

}
