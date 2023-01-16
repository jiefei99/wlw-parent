package com.jike.wlw.service.product.info;

public enum ProtocolType {

    MODBUS("modbus"), OPC_UA("opc-ua"), CUSTOMIZE("customize"), BLE("ble"), ZIGBEE("zigbee");

    private String caption;

    private ProtocolType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
