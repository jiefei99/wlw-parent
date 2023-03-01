/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 *
 * 项目名： chillbaby-web
 * 文件名： e.java
 * 模块说明：
 * 修改历史：
 * 2018年9月25日 - subinzhu - 创建。
 */
package com.jike.wlw.service.author.login.account;


import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改密码信息
 *
 * @author zrs
 */
@Getter
@Setter
@ApiModel("修改密码")
public class AccountUserModifyPwd extends Entity {
  private static final long serialVersionUID = -7405899179191402555L;

  @ApiModelProperty("原密码")
  private String oldPassword;
  @ApiModelProperty("新密码")
  private String newPassword;

  @ApiModelProperty("用户id")
  private String userId;
}
