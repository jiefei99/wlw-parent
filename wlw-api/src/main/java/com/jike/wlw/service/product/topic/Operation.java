package com.jike.wlw.service.product.topic;

import java.io.Serializable;

public enum Operation implements Serializable {
    SUB("sub"), PUB("pub"), ALL("all");

    private String caption;

    private Operation(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
