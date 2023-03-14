package com.jike.wlw.service.upgrade.ota;

/**
 * @title: OTAUpgradePackageJobStatusType
 * @Author RS
 * @Date: 2023/3/14 9:54
 * @Version 1.0
 */
public enum OTAUpgradePackageJobStatusType {

    PLANNED("计划中"),
    IN_PROGRESS("执行中"),
    COMPLETED("已完成"),
    CANCELED("已取消");

    private String caption;

    private OTAUpgradePackageJobStatusType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}


