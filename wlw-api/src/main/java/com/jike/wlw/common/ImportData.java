package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author mengchen
 * @date 2022/9/7
 * @apiNote
 */
@Getter
@Setter
@ApiModel("导入返回参数")
public class ImportData implements Serializable {

    private static final long serialVersionUID = -22392133493169639L;
    @ApiModelProperty("错误上传记录数")
    private int failCount = 0;
    @ApiModelProperty("成功上传记录数")
    private int successCount = 0;
    @ApiModelProperty("忽略上传记录数")
    private int ignoreCount = 0;
    @ApiModelProperty("返回地址")
    private String filePath;

}

