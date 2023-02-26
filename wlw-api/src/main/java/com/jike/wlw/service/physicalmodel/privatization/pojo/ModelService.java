package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.jike.wlw.service.physicalmodel.CallType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: ModelService
 * @Author RS
 * @Date: 2023/2/20 11:18
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class ModelService extends PhysicalModelBase {
    private static final long serialVersionUID = 130312029722547612L;

    @ApiModelProperty("输入参数")
    private ModelIOParm inputParams;
    @ApiModelProperty("输出参数")
    private ModelIOParm outputParams;
    @ApiModelProperty("调用方式")
    private CallType callType;
}


