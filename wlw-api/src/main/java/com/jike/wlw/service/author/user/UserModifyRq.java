/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/6/23 13:45 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user;

import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("修改用户信息")
public class UserModifyRq extends Entity {
    private static final long serialVersionUID = -7977188629951051540L;

    @ApiModelProperty("userId")
    private String userId;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("头像")
    private String headImage;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("冻结状态")
    private FreezeStatus status;
    @ApiModelProperty("备注")
    private String remark;

    //辅助字段
    private String loginId;

}