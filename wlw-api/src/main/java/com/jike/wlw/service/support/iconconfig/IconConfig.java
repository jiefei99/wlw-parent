package com.jike.wlw.service.support.iconconfig;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("图标配置")
public class IconConfig extends StandardEntity {
    private static final long serialVersionUID = -6393904492948086547L;

    @ApiModelProperty("图标链接")
    private String url;
    @ApiModelProperty("图标描述")
    private String description;
    @ApiModelProperty("应用类型")
    private AppType appId;
}
