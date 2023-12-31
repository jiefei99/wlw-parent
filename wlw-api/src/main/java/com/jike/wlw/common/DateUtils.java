package com.jike.wlw.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @title: DateUtils
 * @Author RS
 * @Date: 2023/3/6 15:56
 * @Version 1.0
 */
public class DateUtils {

    //todo 因为阿里返回都UTC时间格式，所以需要转换为东八区时间
    //暂时先写传递时间格式，后期我会改成正则去判断他的格式
    public static Date dealDateFormatUTC(String oldDateStr) {
        Date date = null;
        try {
            oldDateStr = oldDateStr.replace("Z", " UTC");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date oldDate = df.parse(oldDateStr);
            SimpleDateFormat sdf = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date = sdf.parse(oldDate.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date dealDateFormat(String oldDateStr) {
        Date date = null;
        try {
            oldDateStr = oldDateStr.replace("Z", "");
            oldDateStr = oldDateStr.replace("T", " ");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = df.parse(oldDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}


