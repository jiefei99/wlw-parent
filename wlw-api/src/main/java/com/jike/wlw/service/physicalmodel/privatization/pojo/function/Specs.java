package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.privatization.pojo.dataStandard.PhysicalModelDataStandard;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @title: Specs
 * @Author RS
 * @Date: 2023/2/27 15:54
 * @Version 1.0
 */

@Getter
@Setter
public abstract class Specs implements Serializable {

    public abstract Specs convert(String id, Map<String, PhysicalModelDataStandard> map);
}


