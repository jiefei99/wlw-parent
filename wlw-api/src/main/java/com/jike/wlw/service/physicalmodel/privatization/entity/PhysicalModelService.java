package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.EventType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: PhysicalModelService
 * @Author RS
 * @Date: 2023/2/20 10:10
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelService extends PhysicalModelBase {
    private static final long serialVersionUID = 130312029722547601L;

    @ApiModelProperty("方法名")
    private String method;
    @ApiModelProperty("输出参数")
    private List<PhysicalModelIOParm> outputParams;
    @ApiModelProperty("输入参数")
    private List<PhysicalModelIOParm> inputParams;
    @ApiModelProperty("事件类型")
    private CallType callType;
}


