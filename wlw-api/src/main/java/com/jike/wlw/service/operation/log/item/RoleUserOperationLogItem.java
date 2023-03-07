package com.jike.wlw.service.operation.log.item;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/23 14:35- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("角色用户操作日志明细")
public class RoleUserOperationLogItem extends Entity {
    private static final long serialVersionUID = -5225892791183000418L;

    @ApiModelProperty("操作日志ID")
    private String operationLogId;
    @ApiModelProperty("被操作用户ID")
    private String userId;
    @ApiModelProperty("备注")
    private String remake;

}
