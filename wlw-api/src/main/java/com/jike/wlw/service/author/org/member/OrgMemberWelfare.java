package com.jike.wlw.service.author.org.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengzl
 */
@Data
@ApiModel("发福利参数")
public class OrgMemberWelfare {

    @ApiModelProperty("企业组织ID")
    private String orgId;

    @ApiModelProperty("单人充值金额")
    private BigDecimal amount;

    @ApiModelProperty("orgMemberId组织成员id集合")
    private List<String> orgMemberIdIn;
}
