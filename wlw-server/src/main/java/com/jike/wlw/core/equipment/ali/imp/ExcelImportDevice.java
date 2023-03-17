package com.jike.wlw.core.equipment.ali.imp;

import com.jike.wlw.core.support.excel.ExcelBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author mengchen
 * @date 2022/8/31
 * @apiNote
 */
@Getter
@Setter
public class ExcelImportDevice extends ExcelBean {
    private static final long serialVersionUID = -1284031964575187827L;

    public static final String COL_DEVICE_NAME = "DeviceName";
    public static final String COL_NICK_NAME = "Nickname";

    private String deviceName;// 设备名称（imei）, 必填
    private String nickname;// 设备备注名称


    @Override
    public boolean isEmpty() {
        return isEmpty(this);
    }

    @Override
    public Map<String, Object> toValuesMap() {
        return toValuesMap(this);
    }
}
