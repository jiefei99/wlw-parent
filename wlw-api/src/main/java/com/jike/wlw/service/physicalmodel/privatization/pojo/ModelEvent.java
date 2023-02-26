package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: ModelEvent
 * @Author RS
 * @Date: 2023/2/20 11:20
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class ModelEvent extends PhysicalModelBase {
    private static final long serialVersionUID = 130312029722547631L;
    @ApiModelProperty("数据类型")
    private DataType dataType;
    @ApiModelProperty("输出参数")
    private List<ModelIOParm> outputParams;
    @ApiModelProperty("调用方式")
    private EventType eventType;
}


