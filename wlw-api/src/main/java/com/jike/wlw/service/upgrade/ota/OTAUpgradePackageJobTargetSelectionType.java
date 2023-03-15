package com.jike.wlw.service.upgrade.ota;

public enum OTAUpgradePackageJobTargetSelectionType {
    ALL("全量升级"),
    SPECIFIC("定向升级"),
    GROUP("分组升级"),
    GRAY("灰度升级");

    private String caption;

    private OTAUpgradePackageJobTargetSelectionType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
