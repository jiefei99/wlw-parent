package com.jike.wlw.dao.author.tenant;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史： 2020/3/11 11:47- sufengjia - 创建。
 */
@Getter
@Setter
public class PTenant extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = 2082836727269294361L;

    public static final String TABLE_NAME = "wlw_tenant";

    @ApiModelProperty("ID")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("启用状态")
    private String status = EnabledStatus.ENABLED.name();

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
