package com.jike.wlw.service.author.user.employee;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@ApiModel("修改用户信息")
public class EmployeeModifyRq extends Entity {
    private static final long serialVersionUID = -2931046691475903014L;

    @ApiModelProperty("用户ID")
    private String id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("备注")
    private String remark;
    private List<String> roleIds;
}
