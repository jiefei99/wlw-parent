package com.jike.wlw.service.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wza
 * @create 2023/2/17
 */
@Getter
@Setter
@ApiModel("设备导入请求参数")
public class BatchImportRq implements Serializable {
    private static final long serialVersionUID = -8580030149082080444L;

    @ApiModelProperty("所属产品ProductKey")
    public String productKey;
    @ApiModelProperty("文件路径地址")
    public String filePath;
}



