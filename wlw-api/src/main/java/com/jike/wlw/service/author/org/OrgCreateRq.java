package com.jike.wlw.service.author.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2022，所有权利保留。
 * <p>
 * 修改历史：
 * 2022/7/21 9:39- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("组织创建请求参数")
public class OrgCreateRq implements Serializable {
    private static final long serialVersionUID = 1303201038979543934L;

    @ApiModelProperty("组织类型")
    private OrgType orgType;
    @ApiModelProperty("上级组织ID")
    private String upperId;
    @ApiModelProperty("组织名称")
    private String name;
    @ApiModelProperty("备注")
    private String remark;

}
