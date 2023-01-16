package com.jike.wlw.common;

import com.geeker123.rumba.commons.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @title: RandomString
 * @Author RS
 * @Date: 2023/1/10 16:10
 * @Version 1.0
 */

//字符串自定义相关类
public class StringRelevant implements Serializable {
    private static final long serialVersionUID = -2158395377464005696L;

    /*
     * length表示生成字符串的长度
     */
    public static String buildRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //生成Id，每两位添加时间（月-天-时-分-秒的顺序）
    public static String buildId(int length) throws Exception{
        if (length<16){
            throw new IllegalAccessException("id自定义位数不少于16");
        }
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        Calendar now = Calendar.getInstance();
        List timeList= Arrays.asList(StringUtils.leftPad(String.valueOf(now.get(Calendar.SECOND)), 2, '0'),
                StringUtils.leftPad(String.valueOf(now.get(Calendar.MINUTE)), 2, '0'),
                StringUtils.leftPad(String.valueOf(now.get(Calendar.HOUR_OF_DAY)), 2, '0'),
                StringUtils.leftPad(String.valueOf(now.get(Calendar.DAY_OF_MONTH)), 2, '0'),
                StringUtils.leftPad(String.valueOf(now.get(Calendar.MONTH) + 1), 2, '0'));
        int j=0;
        for (int i = 0; i < (length-timeList.size()*2); i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
            if (i%2!=0&&i<10){
                sb.append(timeList.get(j++));
            }
            while (i==(length-(timeList.size()*2+1)) && j<timeList.size()){
                sb.append(timeList.get(j++));
                if (j==timeList.size()){
                    break;
                }
            }
        }
        return sb.toString();
    }

    //    计算长度，中文长度为2
    public static int calcStrLength(String str) {
        int size = 0;
        int charNum;
        for (int i = 0; i < str.length(); i++) {
            charNum = str.charAt(i);
            if (19968 <= charNum && charNum < 40869) {
                size += 2;
            } else {
                size++;
            }
        }
        return size;
    }

}


