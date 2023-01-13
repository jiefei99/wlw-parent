package com.jike.wlw.common;

import java.io.Serializable;
import java.util.Random;

/**
 * @title: RandomString
 * @Author RS
 * @Date: 2023/1/10 16:10
 * @Version 1.0
 */
public class RandomString implements Serializable {
    private static final long serialVersionUID = -2158395377464005696L;


    /*
     * length表示生成字符串的长度
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}


