package com.jike.wlw.service.author.user.employee;

import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: EmployeeModifyStatusRq
 * @Author RS
 * @Date: 2023/3/2 18:26
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("修改用户状态")
public class EmployeeModifyStatusRq extends Entity {
    private static final long serialVersionUID = -2931046691475903314L;

    @ApiModelProperty("用户ID")
    private String id;
    @ApiModelProperty("姓名")
    private FreezeStatus status;
}


