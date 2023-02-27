package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: ArraySpecs
 * @Author RS
 * @Date: 2023/2/27 16:05
 * @Version 1.0
 */

@Getter
@Setter
public class ArraySpecs implements Serializable {
    private int size;

    private List<StructSpecs> specs;
    private ItemSpecs item;


}


