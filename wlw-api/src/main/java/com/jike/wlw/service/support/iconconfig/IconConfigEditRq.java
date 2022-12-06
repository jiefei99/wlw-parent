package com.jike.wlw.service.support.iconconfig;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("新增编辑图标配置请求参数")
public class IconConfigEditRq extends Entity {
    private static final long serialVersionUID = -6197403588709884769L;

    @ApiModelProperty("图标链接")
    private String url;
    @ApiModelProperty("图标描述")
    private String description;
    @ApiModelProperty("应用类型")
    private AppType appId;
}
