package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import com.jike.wlw.service.physicalmodel.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PPhysicalModelDataTypeStandard
 * @Author RS
 * @Date: 2023/2/16 17:39
 * @Version 1.0
 */

@Getter
@Setter
public class PPhysicalModelDataStandard extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -6745778097141439297L;

    public static final String TABLE_NAME = "wlw_physical_model_data_standard";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("父类Id")
    private String parentId;
    @ApiModelProperty("属性类型")
    private DataType type;
    @ApiModelProperty("输入输出参数类型")
    private String ioParmType;
    @ApiModelProperty("最小值")
    private String min;
    @ApiModelProperty("最大值")
    private String max;
    @ApiModelProperty("属性单位")
    private String unit;
    @ApiModelProperty("单位名称")
    private String unitName;
    @ApiModelProperty("数组元素的个数")
    private String size;
    @ApiModelProperty("数组元素类型")
    private DataType arrayType;
    @ApiModelProperty("步长")
    private String step;
    @ApiModelProperty("数据长度")
    private Long length;
    @ApiModelProperty("布尔0描述")
    private String boolZeroRemark;
    @ApiModelProperty("布尔1描述")
    private String boolOneRemark;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


