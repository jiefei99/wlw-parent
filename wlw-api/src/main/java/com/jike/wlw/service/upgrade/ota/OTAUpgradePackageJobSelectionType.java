package com.jike.wlw.service.upgrade.ota;

public enum OTAUpgradePackageJobSelectionType {
    DYNAMIC("动态升级"),
    STATIC("静态升级");

    private String caption;

    private OTAUpgradePackageJobSelectionType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
