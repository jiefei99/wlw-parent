/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月13日 23:28 - ASUS - 创建。
 */
package com.jike.wlw.service.upgrade.ota;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */
public enum OTAUpgradePackageTaskStatusType {

    CONFIRM("待确认"),
    QUEUED("待推送"),
    NOTIFIED("已推送"),
    IN_PROGRESS("升级中"),
    SUCCEEDED("升级成功"),
    FAILED("升级失败"),
    CANCELED("已取消");

    private String caption;

    private OTAUpgradePackageTaskStatusType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
