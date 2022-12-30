package com.jike.wlw.service.equipment;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("设备信息")
public class Equipment extends StandardEntity {
    private static final long serialVersionUID = 1102343985736867036L;

    @ApiModelProperty("设备编号")  //设备三元组deviceName
    private String id;
    @ApiModelProperty("设备三元组deviceSecret")  //设备三元组
    private String deviceSecret;
    @ApiModelProperty("设备三元组productKey")  //设备三元组
    private String productKey;
    @ApiModelProperty("设备名称")
    private String name;
//    @ApiModelProperty("产品编号")
//    private String productId;
    @ApiModelProperty("状态")
    private EquipmentStatus status;
    @ApiModelProperty("MQTT连接参数")
    private MQTTConnection connectMQTT;


    //激活时间、最后上线时间


}
