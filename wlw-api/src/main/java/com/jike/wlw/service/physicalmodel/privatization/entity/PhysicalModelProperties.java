package com.jike.wlw.service.physicalmodel.privatization.entity;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: PhysicalModelProperties
 * @Author RS
 * @Date: 2023/2/20 9:40
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelProperties extends PhysicalModelBase {
    private static final long serialVersionUID = 130322029720547601L;

    @ApiModelProperty("是否必要")
    private boolean required;
    @ApiModelProperty("读写")
    private String rwFlag;
    @ApiModelProperty("子项类型")
    private String childDataType;
    @ApiModelProperty("")
    private String childName;
    @ApiModelProperty("数据")
    private PhysicalModelDataStandardCreateRq dataSpecs;  //INT、FLOAT、DOUBLE、TEXT、DATE、ARRAY
    @ApiModelProperty("数据")
    private List<PhysicalModelDataStandardCreateRq> dataSpecsList; //ENUM、BOOL、STRUCT
}


