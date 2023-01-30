package com.jike.wlw.dao.physicalmodel.function;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PFunction extends PEntity implements JdbcEntity {
    private static final long serialVersionUID = -6795778097141139297L;

    public static final String TABLE_NAME = "wlw_function";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("编号")  //identifier
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("读写类型")
    private String accessMode;
    @ApiModelProperty("是否是标准功能的标准属性")
    private boolean required;

    //属性类型
    @ApiModelProperty("期望值类型")
    private String valueType;
    @ApiModelProperty("期望值")
    private String valueJson;
//    @ApiModelProperty("时间")
//    private Date occurTime;

    //事件、服务类型
    @ApiModelProperty("事件描述/服务描述")
    private String desc;
    @ApiModelProperty("出参")
    private String outputDataJson;
    @ApiModelProperty("入参")  //事件类型独有
    private String inputDataJson;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
