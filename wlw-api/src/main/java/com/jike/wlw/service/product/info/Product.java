package com.jike.wlw.service.product.info;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel("产品信息")
public class Product extends StandardEntity {
    private static final long serialVersionUID = 6355685899103067786L;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("产品secret")
    private String productSecret;
    @ApiModelProperty("物模型编号集合")
    private List<String> physicalModelIds;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("版本类型")
    private String aliyunCommodityCode;
    @ApiModelProperty("认证方式")
    private AuthType authType;
    @ApiModelProperty("品类的标识符")
    private String categoryKey;
    @ApiModelProperty("品类名称")
    private String categoryName;
    @ApiModelProperty("数据格式")
    private Integer dataFormat; //0 透传模式 ; 1 Alink JSON
    @ApiModelProperty("该产品下的设备数量")
    private Integer deviceCount;
    @ApiModelProperty("是否使用id2认证")
    private Boolean id2;
    @ApiModelProperty("调用者是否是产品的拥有者")
    private Boolean owner;
    @ApiModelProperty("连网方式")
    private NetType netType;  //则默认为Wi-Fi
    @ApiModelProperty("节点类型")
    private Integer nodeType;
    @ApiModelProperty("网关协议类型")
    private ProtocolType protocolType;
    @ApiModelProperty("数据校验级别")
    private Integer validateType;
    //产品发布成功后，不可编辑产品、物模型，删除产品。
    @ApiModelProperty("是否发布")
    private PublishStateType ProductStatus;
}
