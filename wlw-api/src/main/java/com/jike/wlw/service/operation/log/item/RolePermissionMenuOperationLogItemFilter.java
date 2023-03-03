package com.jike.wlw.service.operation.log.item;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/23 14:56- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("角色权限菜单操作日志明细查询条件")
public class RolePermissionMenuOperationLogItemFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 6581549697022706311L;

    @ApiModelProperty("操作日志ID等于")
    private String operationLogIdEq;
    // “in”批量查询
    @ApiModelProperty("操作日志ID集合")
    private List<String> operationLogIdIn;

}
