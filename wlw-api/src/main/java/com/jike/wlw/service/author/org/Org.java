package com.jike.wlw.service.author.org;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2022，所有权利保留。
 * <p>
 * 修改历史：
 * 2022/7/20 14:39- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("组织")
public class Org extends StandardEntity {
    private static final long serialVersionUID = -308040813225868498L;

    @ApiModelProperty("组织类型")
    private OrgType orgType;
    @ApiModelProperty("组织名称")
    private String name;
    @ApiModelProperty("上级组织ID")
    private String upperId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("自动生产标示")
    private Integer id;
}
