package com.jike.wlw.service.equipment;

public enum EquipmentStatus {

    UNACTIVE("未激活"), DISABLE("已禁用"), ONLINE("在线"), OFFLINE("离线");

    private String caption;

    private EquipmentStatus(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
