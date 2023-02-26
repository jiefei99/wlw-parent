package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.privatization.pojo.ModelIOParm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @title: PhysicalModelDataStandardCreateRq
 * @Author RS
 * @Date: 2023/2/17 19:48
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelDataStandardCreateRq extends Entity implements Serializable {
    private static final long serialVersionUID = -1698859554278354115L;
    @ApiModelProperty("租户Id")
    private String tenantId;
    @ApiModelProperty("父类Id")
    private String parentId;
    @ApiModelProperty("数据类型")
    private DataType dataType;
    @ApiModelProperty("数据")
    private String dataSpecs;
//    @ApiModelProperty("数据")
//    private List<String> dataSpecsList; //ENUM、BOOL、STRUCT
//    @ApiModelProperty("输入参数")
//    private List<ModelIOParm> inputParams;
//    @ApiModelProperty("输出参数")
//    private List<ModelIOParm> outputParams;
}


