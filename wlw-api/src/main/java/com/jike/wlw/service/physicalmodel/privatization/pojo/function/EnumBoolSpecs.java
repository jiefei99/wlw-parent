package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.alibaba.fastjson.JSON;
import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.security.Key;
import java.util.Map;

/**
 * @title: EnumBoolSpecs
 * @Author RS
 * @Date: 2023/2/27 16:02
 * @Version 1.0
 */

@Getter
@Setter
public class EnumBoolSpecs extends Specs implements Serializable {
    private Map<Integer,String> value;

    @Override
    public EnumBoolSpecs convert(String id, Map<String, PhysicalModelDataStandard> dataStandardMap) {
        EnumBoolSpecs enumBoolSpecs = new EnumBoolSpecs();
        if (dataStandardMap.get(id)!=null){
            Map map = JSON.parseObject(dataStandardMap.get(id).getBoolEnumRemark(), Map.class);
            enumBoolSpecs.setValue(map);
        }
        return enumBoolSpecs;
    }
}


