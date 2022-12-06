package com.jike.wlw.service.author;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("角色查询条件")
public class AuthFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -4585152361405492174L;

    @ApiModelProperty("角色id等于")
    private String roleIdEq;
    @ApiModelProperty("角色id在之中")
    private List<String> roleIdIn;
    @ApiModelProperty("员工id")
    private String userIdEq;
}
