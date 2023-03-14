package com.jike.wlw.service.upgrade.ota;

import java.util.HashMap;
import java.util.Map;

public enum OTAUpgradePackageJobType {

    VERIFY_FIRMWARE("升级包验证批次"),
    UPGRADE_FIRMWARE("批量升级批次");

    private String caption;

    private OTAUpgradePackageJobType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
