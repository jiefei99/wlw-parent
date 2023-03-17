package com.jike.wlw.service.equipment.vo;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: EquipmentByNameVO
 * @Author RS
 * @Date: 2023/3/17 14:53
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("请求参数")
public class EquipmentByNameVO extends Entity implements Serializable {

    private static final long serialVersionUID = 2501498364862758014L;
    private String name;
    private String id;
}


