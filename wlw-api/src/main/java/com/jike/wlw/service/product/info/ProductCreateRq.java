package com.jike.wlw.service.product.info;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel("注册产品请求参数")
public class ProductCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -7698859554276354115L;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("节点类型")   //0:设备    1:网关
    private int nodeType;
    @ApiModelProperty("数据格式")   //0:透传/自定义格式（CUSTOM_FORMAT）    1:Alink协议（ALINK_FORMAT）
    private int dataFormat;
    @ApiModelProperty("设备接入网关的协议类型") //customize自定义协议
    private ProtocolType protocolType;
    @ApiModelProperty("物模型编号集合")
    private List<String> physicalModelIds;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否自动发布物模型")
    private boolean publishAuto=true;
    @ApiModelProperty("连网模式") //不传入此参数，则默认为Wi-Fi
    private NetType netType=NetType.WIFI;
    @ApiModelProperty("产品所属资源组Id")
    private String resourceGroupId;
    @ApiModelProperty("认证方式")
    private AuthType authType;
    @ApiModelProperty("产品品类标识符")
    private String categoryKey;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("入网凭证ID") //NetType选择为LORA时，该参数必需
    private String joinPermissionId;
    @ApiModelProperty("产品版本类型")
    private String aliyunCommodityCode;
    @ApiModelProperty("数据校验级别")   //1:弱校验   2:免校验  (不传默认弱校验)
    private int validateType=1;
    @ApiModelProperty("是否使用ID²认证")  //默认false
    private boolean id2;
}
