package com.jike.wlw.service.author.user.role;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("角色查询条件")
public class RoleFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 8775665257253140054L;

    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("角色名称类似于")
    private String nameLike;
    @ApiModelProperty("角色名称等于")
    private String nameEq;
    @ApiModelProperty("角色id集合")
    private List<String> uuidIn;

}
