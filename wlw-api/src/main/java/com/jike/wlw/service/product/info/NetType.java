package com.jike.wlw.service.product.info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: NetType
 * @Author RS
 * @Date: 2023/1/10 18:12
 * @Version 1.0
 */
public enum NetType implements Serializable {

    LORA("LoRaWAN"), WIFI("Wi-Fi"), CELLULAR("Cellular（2G/3G/4G/5G）蜂窝网"), ETHERNET("Ethernet以太网"), OTHER("其他");

    private String caption;

    private NetType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public final static Map<Integer,NetType> map = new HashMap();
    static {
        map.put(3, NetType.WIFI);
        map.put(6, NetType.CELLULAR);
        map.put(7, NetType.ETHERNET);
        map.put(8, NetType.OTHER);
    }
}


