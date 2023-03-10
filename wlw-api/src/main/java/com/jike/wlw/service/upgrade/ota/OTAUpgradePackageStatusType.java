package com.jike.wlw.service.upgrade.ota;

import java.util.HashMap;
import java.util.Map;

public enum OTAUpgradePackageStatusType {

    NOTVERIFIED("未验证"),
    VERIFIED("已验证"),
    VERIFYING("验证中"),
    VERIFICATIONFAILED("验证失败"),
    UNWANTED("不需要验证");

    private String caption;

    private OTAUpgradePackageStatusType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public final static Map<Integer, OTAUpgradePackageStatusType> map = new HashMap();
    static {
        map.put(0, OTAUpgradePackageStatusType.NOTVERIFIED);
        map.put(1, OTAUpgradePackageStatusType.VERIFIED);
        map.put(2, OTAUpgradePackageStatusType.VERIFYING);
        map.put(3, OTAUpgradePackageStatusType.VERIFICATIONFAILED);
        map.put(-1, OTAUpgradePackageStatusType.UNWANTED);
    }
}
