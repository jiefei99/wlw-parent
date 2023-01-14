/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 项目名：	yinlu-api
 * 文件名：	Tenant.java
 * 模块说明：
 * 修改历史：
 * 2020年3月10日 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.tenant;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenpeisi
 *
 */
@Setter
@Getter
@ApiModel("租户")
public class Tenant extends Entity {
    private static final long serialVersionUID = -5507249360853853281L;

    @ApiModelProperty("编号")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("启用状态")
    private EnabledStatus status = EnabledStatus.ENABLED;

}
