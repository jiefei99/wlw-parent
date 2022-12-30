package com.jike.wlw.service.equipment;

public enum EquipmentStatus {

    INACTIVE("未激活"), ONLINE("在线"), OFFLINE("离线");

    private String caption;

    private EquipmentStatus(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
