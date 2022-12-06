package com.jike.wlw.service.author.org.member;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.jike.wlw.service.author.org.OrgType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Getter
@Setter
@ApiModel("组织成员查询条件")
public class OrgMemberFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7521129327900385003L;

    @ApiModelProperty("关键字")
    private String keywords;
    @ApiModelProperty("店铺启动状态")
    private EnabledStatus statusEq;

    @ApiModelProperty("信息块")
    private String parts;
    @ApiModelProperty("标志like")
    private Integer idLike;
    @ApiModelProperty("uuid等于")
    private String uuidEq;
    @ApiModelProperty("组织ID等于")
    private String orgIdEq;
    @ApiModelProperty("成员工号等于")
    private String numberEq;
    @ApiModelProperty("组织类型等于")
    private OrgType orgTypeEq;
    @ApiModelProperty("手机号等于")
    private String mobileEq;
    @ApiModelProperty("姓名like")
    private String nameLike;
    @ApiModelProperty("用户ID等于")
    private String userIdEq;
    @ApiModelProperty("是否是管理员。true：是")
    private Boolean isAdmin;
    @ApiModelProperty("组织成员启用状态")
    private EnabledStatus status;

    @ApiModelProperty("在组织ID中")
    private List<String> orgIdIn;
    @ApiModelProperty("在用户ID中")
    private List<String> userIdIn;
    @ApiModelProperty("在工号Number中")
    private List<String> numberIn;
    @ApiModelProperty("在uuid中")
    private List<String> uuidIn;
}
