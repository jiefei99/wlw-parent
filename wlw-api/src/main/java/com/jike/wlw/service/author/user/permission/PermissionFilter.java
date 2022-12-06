package com.jike.wlw.service.author.user.permission;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("权限查询条件")
public class PermissionFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -3885642703606789033L;

    @ApiModelProperty("应用ID等于")
    private String appIdEq;
    @ApiModelProperty("权限ID等于")
    private String idEq;
    @ApiModelProperty("权限ID在之中")
    private List<String> idIn;
    @ApiModelProperty("上级ID等于")
    private String groupIdEq;
    @ApiModelProperty("角色ID等于")
    private String roleIdEq;
    @ApiModelProperty("名称等于")
    private String nameEq;

}
