/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	mark-wechatweb
 * 文件名：	EmployeeCreateRq.java
 * 模块说明：
 * 修改历史：
 * 2019年4月27日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.employee;

import com.jike.wlw.service.author.org.OrgType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ApiModel("添加员工请求参数")
public class EmployeeCreateRq implements Serializable {
    private static final long serialVersionUID = -2172900463849804347L;

    @ApiModelProperty("组织ID")
    private String orgId = "-";
    @ApiModelProperty("组织类型")
    private OrgType orgType = OrgType.SYSTEM;
    @ApiModelProperty("登录账号")
    private String loginId;
    @ApiModelProperty("员工姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("角色id集合")
    private List<String> roleIds;
}
