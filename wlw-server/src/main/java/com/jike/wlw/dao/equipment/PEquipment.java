package com.jike.wlw.dao.equipment;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PEquipment extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -7236572503335021856L;

    public static final String TABLE_NAME = "wlw_equipment";

    @ApiModelProperty("设备编号")
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
    private String status;
    @ApiModelProperty("MQTT连接参数")
    private String connectMQTTJson;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
