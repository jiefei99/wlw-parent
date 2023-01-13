package com.jike.wlw.service.product;

import java.io.Serializable;

/**
 * @title: NetType
 * @Author RS
 * @Date: 2023/1/10 18:12
 * @Version 1.0
 */
public enum NetType implements Serializable {

    LORA("LoRaWAN"), WIFI("Wi-Fi"), CELLULAR("cellular"), ETHERNET("ethernet"), OTHER("other");

    private String caption;

    private NetType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}


