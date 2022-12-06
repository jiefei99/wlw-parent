/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名：	eguard-api
 * 文件名：	IdRequest.java
 * 模块说明：
 * 修改历史：
 * 2019年7月12日 - chenpeisi - 创建。
 */
package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author chenpeisi
 */
@Getter
@Setter
@ApiModel("id统一请求参数")
public class IdRequest implements Serializable {
    private static final long serialVersionUID = -3891693889074339795L;

    @ApiModelProperty("id")
    private String id;

}
