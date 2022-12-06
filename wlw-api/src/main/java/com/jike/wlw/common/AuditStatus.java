package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2021，所有权利保留。
 * <p>
 * 修改历史：
 * 2021/10/15 16:45- zhengzhoudong - 创建。
 */

/**
 * 审核状态
 */
public enum AuditStatus {

    INITIAL("待审核"),
    AUDITED("审核通过"),
    BACK("审核不通过"),
    CANCELED("已取消");

    private String caption;

    AuditStatus(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }


}
