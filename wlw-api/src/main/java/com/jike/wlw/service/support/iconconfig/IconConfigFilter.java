package com.jike.wlw.service.support.iconconfig;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("图标配置查询条件")
public class IconConfigFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = 2885380826749919544L;

    @ApiModelProperty("租户ID等于")
    private String tenantIdEq;
    @ApiModelProperty("应用类型等于")
    private AppType appIdEq;
    @ApiModelProperty("描述类似于")
    private String descriptionLike;
}
