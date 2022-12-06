package com.jike.wlw.sys.web.controller.author;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("用户角色请求参数")
public class UserRoleRq extends Entity {
    private static final long serialVersionUID = -5558811548754013308L;

    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("用户id json")
    private String userIdsJson;

}
