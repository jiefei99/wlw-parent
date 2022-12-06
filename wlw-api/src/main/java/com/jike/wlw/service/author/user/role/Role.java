/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/30 15:52 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.role;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("角色")
public class Role extends StandardEntity {
    private static final long serialVersionUID = 9074985292327184903L;

    @ApiModelProperty("角色名称 ")
    private String name;
    @ApiModelProperty("备注")
    private String remark;

}