package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.DataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: DataTypeFactory
 * @Author RS
 * @Date: 2023/2/27 16:35
 * @Version 1.0
 */

@Component
public class SpecsFactory {
    @Autowired
    private static Map<DataType, Specs> specsMap = new HashMap<>();


    public static Specs getInvokeSpecs(DataType type) {
        return specsMap.get(type);
    }

    static {
        specsMap.put(DataType.ENUM, new EnumBoolSpecs());
        specsMap.put(DataType.BOOL, new EnumBoolSpecs());
        specsMap.put(DataType.INT, new NumberSpecs());
        specsMap.put(DataType.FLOAT, new NumberSpecs());
        specsMap.put(DataType.DOUBLE, new NumberSpecs());
        specsMap.put(DataType.DATE, new DateTextSpecs());
        specsMap.put(DataType.TEXT, new DateTextSpecs());

    }
}


