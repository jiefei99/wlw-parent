package com.jike.wlw.service.author.org.member;

import com.jike.wlw.service.author.org.OrgType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2022，所有权利保留。
 * <p>
 * 修改历史：
 * 2022/7/21 14:48- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("组织成员创建请求参数")
public class OrgMemberCreateRq implements Serializable {
    private static final long serialVersionUID = -4840868224171283213L;

    @ApiModelProperty("组织ID")
    private String orgId;
    @ApiModelProperty("成员头像")
    private String image;
    @ApiModelProperty("组织类型")
    private OrgType orgType;
    @ApiModelProperty("成员工号")
    private String number;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("是否是管理员 是：true")
    private Boolean isAdmin = Boolean.FALSE;
    @ApiModelProperty("备注")
    private String remark;

}
