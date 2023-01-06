/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月06日 15:03 - ASUS - 创建。
 */
package com.jike.wlw.core.product.iot;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 *
 * @author rs
 * @since 1.0
 */
@Setter
@Getter
@ApiModel("阿里产品Topic类请求参数")
public class ProductTopicRq implements Serializable {
    private static final long serialVersionUID = 7456101230072538946L;

    @ApiModelProperty("操作权限")
    private String operation;
    @ApiModelProperty("ProductKey")
    private String productKey;
    @ApiModelProperty("Topic类的自定义类目名称")
    private String topicShortName;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("Topic类的ID")
    private String topicId;
    @ApiModelProperty("Topic类的描述信息")
    private String desc;
}
