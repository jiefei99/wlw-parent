package com.jike.wlw.service.author.org.member;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.author.org.Org;
import com.jike.wlw.service.author.org.OrgType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.Store;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2022，所有权利保留。
 * <p>
 * 修改历史：
 * 2022/7/20 15:10- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("组织成员")
public class OrgMember extends StandardEntity {
    private static final long serialVersionUID = -4266348512538457676L;

    /* 信息块：组织信息 */
    public static final String PART_ORG = "org";

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
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("是否是管理员。true：是")
    private Boolean isAdmin;
    @ApiModelProperty("组织启用状态")
    private EnabledStatus status = EnabledStatus.ENABLED;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("自动生成标示")
    private Integer id;
    // 辅助属性
    @ApiModelProperty("组织信息")
    private Org org;
}
