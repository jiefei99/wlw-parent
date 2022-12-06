package com.jike.wlw.service.author.user.permission;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("权限")
public class PermissionRq extends Entity {
    private static final long serialVersionUID = -2254811398077905029L;

    @ApiModelProperty("应用id")  //WEB
    private String appId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("权限id")  //WEB.NOTICE.VIEW
    private String id;
    @ApiModelProperty("分组id")  //NOTICE
    private String groupId;


}
