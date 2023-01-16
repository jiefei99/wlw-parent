package com.jike.wlw.service.product.info;

import java.io.Serializable;

public enum AuthType implements Serializable {
    SECRET("secret"), ID2("id2"), X509("x509");

    private String caption;

    private AuthType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
