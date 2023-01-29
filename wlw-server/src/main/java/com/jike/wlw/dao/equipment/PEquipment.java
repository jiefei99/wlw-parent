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

    @ApiModelProperty("租户")
    public String tenantId;
    @ApiModelProperty("编号")
    public String id;
    @ApiModelProperty("名称")
    public String name;
    @ApiModelProperty("密钥")
    public String secret;
    @ApiModelProperty("所属产品的ProductKey")
    public String productKey;
    @ApiModelProperty("所属产品的名称")
    public String productName;
    @ApiModelProperty("所属资源组ID")
    public String resourceGroupId;
    @ApiModelProperty("设备默认OTA模块版本号")
    public String firmwareVersion;
    @ApiModelProperty("IP地址")
    public String ipAddress;
    @ApiModelProperty("备注名称")
    public String nickname;
    @ApiModelProperty("节点类型")//0：设备，设备不能挂载子设备；1：网关，网关可以挂载子设备
    public int nodeType;
    @ApiModelProperty("是否设备的拥有者")
    public boolean owner;
    @ApiModelProperty("是否已删除")
    public boolean deleted;
    @ApiModelProperty("所在地区")
    public String region;
    @ApiModelProperty("状态")
    public String status;
    @ApiModelProperty("状态变更时间")
    public long timestamp;
    @ApiModelProperty("激活时间(GMT格式)")//2018-08-06 10:48:41
    public String gmtActive;
    @ApiModelProperty("最近上线时间(GMT格式)")
    public String gmtOnline;
    @ApiModelProperty("创建时间(GMT格式)")
    public String gmtCreate;
    @ApiModelProperty("修改时间(GMT格式)")
    public String gmtModified;
    @ApiModelProperty("激活时间(UTC格式)")//2018-08-06T02:48:41.000Z
    public String utcActive;
    @ApiModelProperty("创建时间(UTC格式)")
    public String utcCreate;
    @ApiModelProperty("最近上线时间(UTC格式)")
    public String utcOnline;
    @ApiModelProperty("最后修改时间(UTC格式)")
    public String utcModified;
    //OTA模块版本信息
    @ApiModelProperty("模块名称")
    public String moduleName;
    @ApiModelProperty("模块版本")
    public String moduleVersion;
    //设备统计数据
    @ApiModelProperty("已激活设备数量")
    public long activeCount;
    @ApiModelProperty("设备总数")
    public long deviceCount;
    @ApiModelProperty("在线设备数量")
    public long onlineCount;
    //LoRaWAN设备信息
    @ApiModelProperty("LoRaWAN设备的DevEUI")
    private String devEui;
    @ApiModelProperty("LoRaWAN设备的PIN Code")
    private String pinCode;
    @ApiModelProperty("LoRaWAN设备的入网凭证JoinEui")
    private String joinEui;
    @ApiModelProperty("LoRaWAN设备的AppKey")
    private String appKey;
    @ApiModelProperty("节点类型")
    private String loraNodeType;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
