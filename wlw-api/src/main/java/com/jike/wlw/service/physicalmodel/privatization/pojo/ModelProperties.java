package com.jike.wlw.service.physicalmodel.privatization.pojo;

import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: ModelProperties
 * @Author RS
 * @Date: 2023/2/20 11:14
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class ModelProperties extends PhysicalModelBase {
    private static final long serialVersionUID = 130312029722547611L;

    @ApiModelProperty("读写")
    private String rwFlag;
    @ApiModelProperty("数据类型")
    private DataType dataType;
    @ApiModelProperty("数据")
    private String dataSpecs;  //INT、FLOAT、DOUBLE、TEXT、DATE、ARRAY
    @ApiModelProperty("数据")
    private List<String> dataSpecsList; //ENUM、BOOL、STRUCT
}


