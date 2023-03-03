/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 *
 * 项目名：	chillbaby-web
 * 文件名：	LoginAccount.java
 * 模块说明：
 * 修改历史：
 * 2018年7月12日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.login.account;


import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.common.FreezeState;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录账号|接口对象
 *
 * @author chenpeisi
 *
 */
@Setter
@Getter
@ApiModel("登录账号")
public class AccountUser extends StandardEntity {
  private static final long serialVersionUID = -323870501267400567L;

  @ApiModelProperty("租户")
  private UserType tenantId;
  @ApiModelProperty("用户类型")
  private UserType userType;
  @ApiModelProperty("用户ID")
  private String userId;
  @ApiModelProperty("昵称")
  private String nickname;
  @ApiModelProperty("用户头像")
  private String headImage;
  @ApiModelProperty("性别")
  private String sex;
  @ApiModelProperty("账号状态")
  private FreezeState state = FreezeState.NORMAL;

  @ApiModelProperty("登录账号")
  private String loginId;
  @ApiModelProperty("登录密码")
  private String password;


}
