package com.jike.wlw.service.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("配置组")
public class ConfigGroup implements Serializable {
    private static final long serialVersionUID = -8330772111371294758L;

    @ApiModelProperty("配置项目")
    private List<Config> configs = new ArrayList<Config>();

    @Transient
    public String getValue(String key) {
        if (configs != null) {
            for (Config config : configs) {
                if (config.getConfigKey().equals(key)) {
                    return config.getConfigValue();
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
