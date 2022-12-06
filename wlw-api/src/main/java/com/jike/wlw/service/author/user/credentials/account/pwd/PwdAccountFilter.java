/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 14:34 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials.account.pwd;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 密码账户查询条件
 */
@Getter
@Setter
@ApiModel("密码账户查询条件")
public class PwdAccountFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -8649364448456180475L;

    @ApiModelProperty("用户类型等于")
    private UserType userTypeEq;
    @ApiModelProperty("用户ID等于")
    private String userIdEq;
    @ApiModelProperty("登录账号等于")
    private String loginIdEq;
    @ApiModelProperty("用户ID包含于")
    private List<String> userIdIn;

}