package com.jike.wlw.service.physicalmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wza
 * @create 2023/3/20
 */
@Getter
@Setter
@ApiModel
public class Params implements Serializable {
    private static final long serialVersionUID = -6490873139815417637L;

    @ApiModelProperty("数据类型")
    private String dataType;//必填，ARRAY、STRUCT、INT、FLOAT、DOUBLE、TEXT、DATE、ENUM、BOOL
    @ApiModelProperty("标识符")
    private String identifier;//必填，可包含大小写英文字母、数字、下划线（_），长度不超过50个字符
    @ApiModelProperty("属性名称")
    private String name;//必填，可包含中文、大小写英文字母、数字、短划线（-）、下划线（_）和半角句号（.），且必须以中文、英文字母或数字开头，长度不超过30个字符，一个中文计为一个字符
    @ApiModelProperty("参数类型")
    private String direction;//必填，PARAM_INPUT：输入参数。PARAM_OUTPUT：输出参数。
    @ApiModelProperty("参数的序号")
    private Integer paraOrder;//必填，从0开始排序，且不能重复
    @ApiModelProperty("参数是否隶属于自定义功能")
    private String custom;//必填
    @ApiModelProperty("数据规范")//非列表型（INT、FLOAT、DOUBLE、TEXT、DATE、ARRAY）的数据规范存储在dataSpecs中
    private DataSpecs dataSpecs;
    @ApiModelProperty("数据规范")//列表型（ENUM、BOOL、STRUCT）的数据规范存储在dataSpecsList中
    private List<DataSpecsTwo> dataSpecsList;

}
