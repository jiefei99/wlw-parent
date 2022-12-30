package com.jike.wlw.dao.product;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PProduct extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -8107661687477264763L;

    public static final String TABLE_NAME = "wlw_product";

    //三元组
//    @ApiModelProperty("编号") //deviceName：可以自定义，否则自动生成
//    private String id;
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

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
