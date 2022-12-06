/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 14:32 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.credentials.account.pwd;

import com.geeker123.rumba.jpa.api.entity.Entity;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 密码账户
 */
@Data
@ApiModel("密码账户")
public class PwdAccount extends Entity {
    private static final long serialVersionUID = 6573798413765401199L;

    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("备注")
    private String remark;
    //登录凭证
    @ApiModelProperty("登录账号")
    private String loginId;
    @ApiModelProperty("登录密码")
    private String password;

}