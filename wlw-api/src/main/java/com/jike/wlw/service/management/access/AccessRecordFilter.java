package com.jike.wlw.service.management.access;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AccessRecordFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 5977997392771443039L;

    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("ip等于")
    private String ipEq;
    @ApiModelProperty("动作等于")
    private String actionEq;
    @ApiModelProperty("浏览记录状态等于")
    private AccessRecordStatus statusEq;
    @ApiModelProperty("浏览记录状态包含于")
    private List<String> statusIn;
    @ApiModelProperty("登陆的用户ID等于")
    private String loginUserIdEq;
    @ApiModelProperty("登陆的用户ID包含于")
    private List<String> loginUserIdIn;
    @ApiModelProperty("登陆的用户名类似于")
    private String loginUserNameLike;
    @ApiModelProperty("创建时间大于等于")
    private Date createdGte;
    @ApiModelProperty("创建时间小于等于")
    private Date createdLte;

}
