package com.jike.wlw.service.author.user;

import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ApiModel("用户查询条件")
public class UserFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 312449838906365406L;

    @ApiModelProperty("用户类型")
    private UserType userTypeEq;
    @ApiModelProperty("在用户id之中")
    private List<String> userIdIn;
    @ApiModelProperty("冻结状态")
    private FreezeStatus statusEq;
    @ApiModelProperty("手机号码等于")
    private String mobileEq;
    @ApiModelProperty("在手机号码之中")
    private List<String> mobileIn;

    // 时间区间查询
    @ApiModelProperty("用户创建时间大于等于")
    private Date createdDateGte;
    @ApiModelProperty("用户创建时间小于等于")
    private Date createdDateLte;

}