package com.jike.wlw.service.physicalmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/3/17
 */
@Setter
@Getter
@ApiModel
public class FunctionBlock implements Serializable {
    private static final long serialVersionUID = -8625538952199380793L;

    @ApiModelProperty("所属产品的ProductKey")
    private String productKey;
    @ApiModelProperty("自定义模块标识符")
    private String functionBlockId;
    @ApiModelProperty("自定义模块名称")
    private String functionBlockName;
    @ApiModelProperty("创建时间戳")
    private String gmtCreated;
    @ApiModelProperty("备注")
    private String description;
}
