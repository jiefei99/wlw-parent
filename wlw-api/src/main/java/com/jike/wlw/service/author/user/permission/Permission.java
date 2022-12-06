package com.jike.wlw.service.author.user.permission;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("权限")
public class Permission extends StandardEntity {
    private static final long serialVersionUID = -3885642703606789033L;

    @ApiModelProperty("应用ID，如：wxa、web...")
    private String appId;
    @ApiModelProperty("权限ID")
    private String id;
    @ApiModelProperty("分组id")
    private String groupId;
    @ApiModelProperty("名称")
    private String name;

}
