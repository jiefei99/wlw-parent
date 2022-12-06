package com.jike.wlw.service.author.user.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ApiModel("新增权限请求参数")
public class PermissionSaveRq implements Serializable {
    private static final long serialVersionUID = -2841650969982862331L;

    @ApiModelProperty("权限参数")
    private List<PermissionRq> permissionRqList;

}
