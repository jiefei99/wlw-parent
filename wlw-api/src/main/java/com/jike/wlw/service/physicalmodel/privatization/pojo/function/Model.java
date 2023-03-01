package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.physicalmodel.DataType;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: Model
 * @Author RS
 * @Date: 2023/2/27 16:29
 * @Version 1.0
 */

@Getter
@Setter
public class Model extends Entity {
    private DataType type;
    private String details;
    private String name;
    private String identifier;
    private boolean required;
}


