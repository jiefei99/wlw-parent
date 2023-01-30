package com.jike.wlw.service.product.info;

import java.util.HashMap;
import java.util.Map;

public enum ProtocolType {

    MODBUS("Modbus协议"),
    OPC_UA("OPC UA协议"),
    CUSTOMIZE("自定义协议"),
    BLE("BLE协议"),
    ZIGBEE("ZigBee协议");

    private String caption;
    private ProtocolType(String caption) {
        this.caption = caption;
    }
    public String getCaption() {
        return caption;
    }

    public final static Map<String,ProtocolType> map = new HashMap();
    static {
        map.put("modbus", ProtocolType.MODBUS);
        map.put("opc-ua", ProtocolType.OPC_UA);
        map.put("customize", ProtocolType.CUSTOMIZE);
        map.put("ble", ProtocolType.BLE);
        map.put("zigbee", ProtocolType.ZIGBEE);
    }
}
