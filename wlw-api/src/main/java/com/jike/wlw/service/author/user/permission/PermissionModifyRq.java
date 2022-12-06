package com.jike.wlw.service.author.user.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel("修改权限请求参数")
public class PermissionModifyRq implements Serializable {
    private static final long serialVersionUID = -6722402463460784455L;

    @ApiModelProperty("权限参数")
    private List<PermissionRq> permissionRqList;
}
