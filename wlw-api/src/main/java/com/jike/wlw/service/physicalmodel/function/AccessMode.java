package com.jike.wlw.service.physicalmodel.function;

public enum AccessMode {

    READONLY("只读"), READANDWRITE("读写");

    private String caption;

    private AccessMode(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
