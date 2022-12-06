package com.jike.wlw.service.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/7/21- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("未读消息统计")
public class unReadNum implements Serializable {
    private static final long serialVersionUID = 9113187862489140844L;

    @ApiModelProperty("订单消息未读数量")
    private long orderCount;
    @ApiModelProperty("活动公告消息未读数量")
    private long activityCount;
    @ApiModelProperty("系统消息未读数量")
    private long systemCount;

}
