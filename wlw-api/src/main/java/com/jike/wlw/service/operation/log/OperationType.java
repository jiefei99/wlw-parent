package com.jike.wlw.service.operation.log;

import io.swagger.annotations.ApiModel;

/**
 *
 */
@ApiModel("操作类型")
public enum OperationType {

    ROLE_PERMISSION_MENU("角色权限菜单"),
    ROLE_USER("角色用户");


    private String caption;

    private OperationType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }

}
