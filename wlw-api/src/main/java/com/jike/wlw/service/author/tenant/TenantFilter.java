/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 项目名：	yinlu-api
 * 文件名：	TenantFilter.java
 * 模块说明：
 * 修改历史：
 * 2020年3月10日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.tenant;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author chenpeisi
 *
 */
@Setter
@Getter
@ApiModel("租户查询条件")
public class TenantFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -5642762189809719383L;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("启用状态")
    private EnabledStatus status;
    @ApiModelProperty("租户ID集合")
    private List<String> ids;

}
