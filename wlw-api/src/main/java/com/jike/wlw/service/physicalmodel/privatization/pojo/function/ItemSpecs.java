package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.DataType;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: Item
 * @Author RS
 * @Date: 2023/2/27 16:11
 * @Version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemSpecs implements Serializable {
    private DataType type;
}


