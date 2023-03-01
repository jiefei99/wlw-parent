/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/12 14:54 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user;

import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("用户")
public class User extends StandardEntity {
    private static final long serialVersionUID = -2061115228482102227L;

    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("头像")
    private String headImage;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("冻结状态")
    private FreezeStatus status = FreezeStatus.NORMAL;
    @ApiModelProperty("备注")
    private String remark;

    //辅助字段
    @ApiModelProperty("登录账户")
    private String loginId;
    @ApiModelProperty("登录密码")
    private String password;
}
