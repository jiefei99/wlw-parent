package com.jike.wlw.service.physicalmodel.privatization.pojo.module;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: PhysicalModelModuleFilter
 * @Author RS
 * @Date: 2023/2/24 10:08
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel
public class PhysicalModelModuleFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -1698859554276344219L;

    @ApiModelProperty("productKey")
    private String productKey;
    @ApiModelProperty("模块名称")
    private String nameEq;
    @ApiModelProperty("模块表示符")
    private String identifierEq;
    @ApiModelProperty("租户Id")
    private String tenantId;
}


