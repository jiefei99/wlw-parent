package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: InOutputData
 * @Author RS
 * @Date: 2023/2/27 18:01
 * @Version 1.0
 */

@Getter
@Setter
public class InOutputData implements Serializable {
    private String identifier;
    private String name;
    private DataAndType dataType;
}


