/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	chillbaby-wechatweb
 * 文件名：	EmployeeFilter.java
 * 模块说明：
 * 修改历史：
 * 2018年7月12日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user.employee;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.jike.wlw.common.FreezeState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel("员工查询条件")
public class EmployeeFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 1697581504911263617L;

    @ApiModelProperty("fetch信息块")
    private String parts;
    @ApiModelProperty("关键字")
    private String keywords;
    @ApiModelProperty("冻结状态")
    private FreezeState freezeStateEq;

    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("ID等于")
    private String idEq;
    @ApiModelProperty("是否为管理员等于")
    private Boolean adminEq;
    @ApiModelProperty("用户ID等于")
    private String userIdEq;
    // “in”批量查询
    @ApiModelProperty("租户ID集合")
    private List<String> tenentIdIn;
    @ApiModelProperty("ID集合")
    private List<String> idIn;
    @ApiModelProperty("用户ID集合")
    private List<String> userIdIn;


}
