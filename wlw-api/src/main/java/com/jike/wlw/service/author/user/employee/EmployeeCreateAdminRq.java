/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	mark-wechatweb
 * 文件名：	EmployeeCreateAdminRq.java
 * 模块说明：
 * 修改历史：
 * 2019年4月24日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.employee;

import com.jike.wlw.service.author.org.OrgType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("创建管理员账号")
public class EmployeeCreateAdminRq implements Serializable {
    private static final long serialVersionUID = -8152807998004326891L;

    @ApiModelProperty("组织类型")
    private OrgType orgType = OrgType.SYSTEM;
    @ApiModelProperty("组织ID")
    private String orgId = "-";
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("登录账号")
    private String loginId;
    @ApiModelProperty("登录密码")
    private String password;

}
