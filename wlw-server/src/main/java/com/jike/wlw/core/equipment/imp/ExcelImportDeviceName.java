package com.jike.wlw.core.equipment.imp;

import com.jike.wlw.core.support.excel.ExcelBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author RS
 * @date 2022/8/31
 * @apiNote
 */
@Getter
@Setter
public class ExcelImportDeviceName extends ExcelBean {
    private static final long serialVersionUID = -1284031964575187827L;

    public static final String COL_DEVICE_NAME = "DeviceName";

    private String deviceName;// 设备名称, 必填


    @Override
    public boolean isEmpty() {
        return isEmpty(this);
    }

    @Override
    public Map<String, Object> toValuesMap() {
        return toValuesMap(this);
    }
}
