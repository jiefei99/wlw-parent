package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.jike.wlw.service.physicalmodel.DataType;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: PropertiesModel
 * @Author RS
 * @Date: 2023/2/27 16:25
 * @Version 1.0
 */

@Getter
@Setter
public class PropertiesModel extends Model {
    private String rwFlag;
    private DataType arrayType;
    private int arraySize;
    private DataAndType dataType;
}


