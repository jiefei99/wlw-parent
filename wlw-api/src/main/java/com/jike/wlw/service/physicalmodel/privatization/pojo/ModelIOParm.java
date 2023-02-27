package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: ModelIOParm
 * @Author RS
 * @Date: 2023/2/20 11:25
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class ModelIOParm extends PhysicalModelBase {
    private static final long serialVersionUID = 130312029722547641L;
    @ApiModelProperty("数据与类型")
    private String dataType;
    @ApiModelProperty("输入|输出参数")
    private String direction;
    @ApiModelProperty("参数的序号")
    private Integer paraOrder;
}


