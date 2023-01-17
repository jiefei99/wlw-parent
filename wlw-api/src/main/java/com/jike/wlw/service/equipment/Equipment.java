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

    @ApiModelProperty("名称")
    public String deviceName;
    @ApiModelProperty("密钥")
    public String deviceSecret;
    @ApiModelProperty("设备默认OTA模块版本号")
    public String firmwareVersion;
    @ApiModelProperty("ID")
    public String iotId;
    @ApiModelProperty("IP地址")
    public String ipAddress;
    @ApiModelProperty("备注名称")
    public String nickname;
    @ApiModelProperty("节点类型")//0：设备，设备不能挂载子设备；1：网关，网关可以挂载子设备
    public Integer nodeType;
    @ApiModelProperty("是否设备的拥有者")
    public Boolean owner;
    @ApiModelProperty("所属产品的ProductKey")
    public String productKey;
    @ApiModelProperty("所属产品的名称")
    public String productName;
    @ApiModelProperty("所在地区")
    public String region;
    @ApiModelProperty("状态")
    public String status;
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


}
