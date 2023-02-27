package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: NumberSpecs
 * @Author RS
 * @Date: 2023/2/27 15:58
 * @Version 1.0
 */

@Getter
@Setter
public class NumberSpecs extends Specs implements Serializable {
    @ApiModelProperty("是否是自定义功能")
    private boolean custom;
    @ApiModelProperty("类型")
    private DataType dataType;
    @ApiModelProperty("最大值")
    private String max;
    @ApiModelProperty("最小值")
    private String min;
    @ApiModelProperty("步长")
    private String step;
    @ApiModelProperty("单位符号")
    private String unit;
    @ApiModelProperty("单位名称")
    private String unitName;

    @Override
    public NumberSpecs convert(String id, Map<String, PhysicalModelDataStandard> dataStandardMap) {
        NumberSpecs numberSpecs=new NumberSpecs();
        if (dataStandardMap.get(id)!=null){
            numberSpecs.setDataType(dataStandardMap.get(id).getType());
            numberSpecs.setMax(dataStandardMap.get(id).getMax());
            numberSpecs.setMin(dataStandardMap.get(id).getMin());
            numberSpecs.setStep(dataStandardMap.get(id).getStep());
            numberSpecs.setUnit(dataStandardMap.get(id).getUnit());
            numberSpecs.setUnitName(dataStandardMap.get(id).getUnitName());
        }
        return numberSpecs;
    }
}


