package com.jike.wlw.dao.product;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import com.jike.wlw.service.product.AuthType;
import com.jike.wlw.service.product.NetType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PProduct extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264763L;

    public static final String TABLE_NAME = "wlw_product";

    //三元组
//    @ApiModelProperty("编号") //deviceName：可以自定义，否则自动生成
//    private String id;
    @ApiModelProperty("产品Id")
    private String productId;
    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("产品secret")
    private String productSecret;
    @ApiModelProperty("物模型编号集合json")
    private String physicalModelIdsJson;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("认证方式")
    public String authType;
    @ApiModelProperty("品类的标识符")
    public String categoryKey;
    @ApiModelProperty("数据格式")
    public Integer dataFormat;
    @ApiModelProperty("是否使用id2认证")
    public Boolean id2;
    @ApiModelProperty("连网方式")
    public NetType netType;
    @ApiModelProperty("节点类型")
    public int nodeType;
    @ApiModelProperty("产品状态")
    public String productStatus;
    @ApiModelProperty("入网凭证ID")
    public String joinPermissionId;
    @ApiModelProperty("网关协议类型")
    public String protocolType;
    @ApiModelProperty("资源组Id")
    public String resourceGroupId;
    @ApiModelProperty("数据校验级别")
    public int validateType;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
