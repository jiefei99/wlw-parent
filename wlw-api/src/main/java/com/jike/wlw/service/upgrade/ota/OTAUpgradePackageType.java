/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月13日 22:55 - ASUS - 创建。
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
public enum OTAUpgradePackageType {


    FullOTA("整包"),
    IncrementOTA("差分");

    private String caption;

    private OTAUpgradePackageType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public final static Map<Integer, OTAUpgradePackageType> map = new HashMap();
    public final static Map<OTAUpgradePackageType, Integer> convertMap = new HashMap();
    static {
        map.put(0, OTAUpgradePackageType.FullOTA);
        map.put(1, OTAUpgradePackageType.IncrementOTA);
        convertMap.put(OTAUpgradePackageType.FullOTA,0);
        convertMap.put(OTAUpgradePackageType.IncrementOTA,1);
    }
}
