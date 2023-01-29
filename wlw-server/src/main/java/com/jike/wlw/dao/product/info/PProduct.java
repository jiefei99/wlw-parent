package com.jike.wlw.dao.product.info;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import com.jike.wlw.service.product.info.NetType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PProduct extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264763L;

    public static final String TABLE_NAME = "wlw_product";

    @ApiModelProperty("产品Id")
    private String id;
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
    private String authType;
    @ApiModelProperty("品类的标识符")
    private String categoryKey;
    @ApiModelProperty("数据格式")
    private Integer dataFormat;
    @ApiModelProperty("是否使用id2认证")
    private Boolean id2;
    @ApiModelProperty("连网方式")
    private NetType netType;
    @ApiModelProperty("节点类型")
    private int nodeType;
    @ApiModelProperty("产品状态")
    private String productStatus;
    @ApiModelProperty("入网凭证ID")
    private String joinPermissionId;
    @ApiModelProperty("网关协议类型")
    private String protocolType;
    @ApiModelProperty("资源组Id")
    private String resourceGroupId;
    @ApiModelProperty("数据校验级别")
    private int validateType;
    @ApiModelProperty("是否发布") //0为未发布，1为发布
    private int isRelease;
    @ApiModelProperty("逻辑删除")
    private int isDeleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
