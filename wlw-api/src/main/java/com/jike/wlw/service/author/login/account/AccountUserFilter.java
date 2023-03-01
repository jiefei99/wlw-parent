/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	mark-web
 * 文件名：	WechatUserFilter.java
 * 模块说明：
 * 修改历史：
 * 2019年6月13日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.login.account;


import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.jike.wlw.common.FreezeState;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zrs
 */
@Setter
@Getter
@ApiModel("用户查询条件")
public class AccountUserFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -8412462553737892254L;
    private int pageSize = 15;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户ID集合")
    private List<String> userIds;
    @ApiModelProperty("账号状态")
    private FreezeState state;

    @ApiModelProperty("登录账号")
    private String loginId;

}
