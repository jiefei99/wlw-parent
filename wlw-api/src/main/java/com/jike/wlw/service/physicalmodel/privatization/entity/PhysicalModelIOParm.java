package com.jike.wlw.service.physicalmodel.privatization.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: PhysicalModelIOParm
 * @Author RS
 * @Date: 2023/2/20 10:14
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelIOParm extends PhysicalModelBase {
    private static final long serialVersionUID = 130322029720547611L;

    @ApiModelProperty("数据")
    private List<PhysicalModelDataStandardCreateRq> dataSpecs;
    @ApiModelProperty("数据")
    private List<PhysicalModelDataStandardCreateRq> dataSpecsList;
}


