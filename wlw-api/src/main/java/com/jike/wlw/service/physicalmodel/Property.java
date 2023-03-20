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
public class Property implements Serializable {
    private static final long serialVersionUID = -8316696292269951135L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("功能创建的时间戳")
    private Long createTs;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("数据类型")//ARRAY、STRUCT、INT、FLOAT、DOUBLE、TEXT、DATE、ENUM、BOOL不同数据类型，可传入的参数不同。
    private String dataType;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("操作类型")//READ_WRITE：读写。READ_ONLY：只读。
    private String rwFlag;
    @ApiModelProperty("是否是标准品类的必选属性")
    private Boolean required;
    @ApiModelProperty("是否是自定义功能")
    private Boolean custom;
    //dataSpecs、dataSpecsList为数据规范。除属性、服务、事件和参数定义数据以外，其他数据都属于数据规范,dataSpecs和dataSpecsList之中必须传入且只能传入一个
    @ApiModelProperty("数据规范")//非列表型（INT、FLOAT、DOUBLE、TEXT、DATE、ARRAY）的数据规范存储在dataSpecs中
    private DataSpecs dataSpecs;
    @ApiModelProperty("数据规范")//列表型（ENUM、BOOL、STRUCT）的数据规范存储在dataSpecsList中
    private List<DataSpecsTwo> dataSpecsList;

}
