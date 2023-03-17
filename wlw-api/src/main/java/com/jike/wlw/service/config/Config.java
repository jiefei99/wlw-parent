package com.jike.wlw.service.config;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("配置")
public class Config extends StandardEntity {
    private static final long serialVersionUID = 5358143370896478171L;

    @ApiModelProperty("配置组")
    private String configGroup;
    @ApiModelProperty("配置组下的key")
    private String configKey;
    @ApiModelProperty("配置的名字")
    private String configName;
    @ApiModelProperty("配置key对应的值")
    private String configValue;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("租户")
    private String tenant;

}