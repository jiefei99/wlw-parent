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
public class Event implements Serializable {
    private static final long serialVersionUID = 6134899336277381572L;

    @ApiModelProperty("productKey")
    private String productKey;//必填
    @ApiModelProperty("功能创建的时间戳")
    private Long createTs;
    @ApiModelProperty("标识符")
    private String identifier;//必填
    @ApiModelProperty("事件名称")
    private String eventName;//必填
    @ApiModelProperty("事件类型")
    private String eventType;//必填
    @ApiModelProperty("输出参数")
    private List<Params> outputdata;
    @ApiModelProperty("是否是标准品类的必选属性")
    private Boolean required;//必填
    @ApiModelProperty("是否是自定义功能")
    private Boolean custom;//必填
}
