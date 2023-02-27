package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.DataType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: DataType
 * @Author RS
 * @Date: 2023/2/27 15:53
 * @Version 1.0
 */

@Getter
@Setter
public class DataAndType implements Serializable {
    private DataType type;
    private Object specs;
}


