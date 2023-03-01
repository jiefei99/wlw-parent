package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2021，所有权利保留。
 * <p>
 * 修改历史：
 * 2021/9/7 11:46- zrs - 创建。
 */
@ApiModel("冻结状态")
public enum FreezeState {
    normal("正常"),
    freezed("已冻结");

    private String caption;

    private FreezeState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }
}
