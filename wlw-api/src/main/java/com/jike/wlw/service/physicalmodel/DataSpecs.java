package com.jike.wlw.service.physicalmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wza
 * @create 2023/3/17
 */
@Getter
@Setter
@ApiModel
public class DataSpecs implements Serializable {
    private static final long serialVersionUID = 1800112445891755898L;
//INT、FLOAT、DOUBLE、   TEXT、DATE、ARRAY）
    @ApiModelProperty("是否是自定义功能")
    private Boolean custom;//必填
    @ApiModelProperty("数据结构类型")
    private String dataType;//必填

    //dataType为INT、FLOAT、DOUBLE
    @ApiModelProperty("最大值")
    private String max;//必填
    @ApiModelProperty("最小值")
    private String min;//必填
    @ApiModelProperty("步长，数据每次变化的增量")
    private String step;//必填
    @ApiModelProperty("精度")
    private String precise;
    @ApiModelProperty("默认值")//dataType为DATE、TEXT也有该值
    private String defaultValue;
    @ApiModelProperty("单位的符号")
    private String unit;//必填
    @ApiModelProperty("单位的名称")
    private String unitName;//必填

    //dataType为DATE、TEXT
    @ApiModelProperty("数据长度")
    private Long length;//必填,取值不能超过2048，单位：字
    @ApiModelProperty("备注")
    private Integer id;

    //dataType为ARRAY
    @ApiModelProperty("数组中的元素的数据类型")
    private String childDataType;//必填
    @ApiModelProperty("数组中的元素个数")
    private Long size;//必填
    @ApiModelProperty("数据规范")
    private DataSpecs dataSpecs;
    @ApiModelProperty("数据规范")
    private List<DataSpecsTwo> dataSpecsList;
}
