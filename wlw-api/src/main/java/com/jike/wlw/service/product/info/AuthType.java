package com.jike.wlw.service.product.info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum AuthType implements Serializable {
    SECRET("使用设备密钥进行设备身份认证"),
    ID2("使用物联网设备身份认证ID²"),
    X509("使用设备X.509证书进行设备身份认证");

    private String caption;

    private AuthType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public final static Map<String,AuthType> map = new HashMap();
    static {
        map.put("secret", AuthType.SECRET);
        map.put("x509", AuthType.X509);
        map.put("id2", AuthType.ID2);
    }
}
