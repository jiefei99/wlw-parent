package com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PhysicalModelDataStandards
 * @Author RS
 * @Date: 2023/2/27 10:21
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelDataStandard extends Entity {

    private static final long serialVersionUID = 130322029721547201L;

    @ApiModelProperty("父类Id")
    private String parentId;
    @ApiModelProperty("属性类型")
    private DataType type;
    @ApiModelProperty("最小值")
    private String min;
    @ApiModelProperty("最大值")
    private String max;
    @ApiModelProperty("属性单位")
    private String unit;
    @ApiModelProperty("单位名称")
    private String unitName;
    @ApiModelProperty("步长")
    private String step;
    @ApiModelProperty("数据长度")
    private Long length;
    @ApiModelProperty("布尔枚举描述")
    private String boolEnumRemark;
}


