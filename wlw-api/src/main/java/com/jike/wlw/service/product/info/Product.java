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

    public static final int RELEASE = 1;//发布
    public static final int UN_RELEASE = 0;//未发布

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
    public String aliyunCommodityCode;
    @ApiModelProperty("认证方式")
    public String authType;
    @ApiModelProperty("品类的标识符")
    public String categoryKey;
    @ApiModelProperty("品类名称")
    public String categoryName;
    @ApiModelProperty("数据格式")
    public Integer dataFormat;
    @ApiModelProperty("是否使用id2认证")
    public Boolean id2;
    @ApiModelProperty("连网方式")
    public Integer netType;
    @ApiModelProperty("节点类型")
    public Integer nodeType;
    @ApiModelProperty("产品状态")
    public String productStatus;
    @ApiModelProperty("网关协议类型")
    public String protocolType;
    @ApiModelProperty("数据校验级别")
    public Integer validateType;
    //产品发布成功后，不可编辑产品、物模型，删除产品。
    @ApiModelProperty("是否发布")
    private int isRelease;
}
