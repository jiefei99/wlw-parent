package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: DateTextSpecs
 * @Author RS
 * @Date: 2023/2/27 16:03
 * @Version 1.0
 */

@Getter
@Setter
public class DateTextSpecs extends Specs implements Serializable {

    private Long length;

    @Override
    public DateTextSpecs convert(String id, Map<String, PhysicalModelDataStandard> dataStandardMap) {
        DateTextSpecs dateTextSpecs=new DateTextSpecs();
        if (dataStandardMap.get(id)!=null){
            dateTextSpecs.setLength(dataStandardMap.get(id).getLength());
        }
        return dateTextSpecs;
    }
}


