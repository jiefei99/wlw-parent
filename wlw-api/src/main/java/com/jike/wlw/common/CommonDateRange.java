package com.jike.wlw.common;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.DateUtil;
import com.geeker123.rumba.jpa.api.range.DateRange;
import io.swagger.annotations.ApiModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2021，所有权利保留。
 * <p>
 * 修改历史：
 * 2021/11/8 15:33- zhengzhoudong - 创建。
 */
@ApiModel("常用时间范围")
public enum CommonDateRange {
    NEARLYDAY("近24小时"),
    YESTERDAY("昨天"),
    TODAY("今天"),
    NEARLYWEEK("近一周"),
    LASTWEEK("上周"),
    WEEK("周"),
    NEARLYMONTH("近30天"),
    LASTMONTH("上月"),
    MONTH("本月"),
    NEARLYTWOMONTHS("近60天"),
    NEARLYQUARTER("近3个月"),
    LASTQUARTER("上个季度"),
    QUARTER("季度"),
    NEARLYYEAR("近一年"),
    LASTYEAR("去年"),
    YEAR("年度");

    private String caption;

    private CommonDateRange(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public DateRange getDateRange() {
        Date current = new Date();
        Date before = null;
        DateRange dateRange = null;
        switch (this) {
            case NEARLYDAY:
                dateRange = new DateRange(DateUtil.addDays(current, -1), current);
                break;
            case YESTERDAY:
                dateRange = new DateRange(DateUtil.getBeginDayOfYesterday(), DateUtil.getEndDayOfYesterDay());
                break;
            case TODAY:
                dateRange = new DateRange(DateUtil.getDayBegin(), DateUtil.getDayEnd());
                break;
            case NEARLYWEEK:
                dateRange = new DateRange(DateUtil.addDays(current, -7), current);
                break;
            case LASTWEEK:
                before = DateUtil.addDays(DateUtil.getWeekFirstDay(current), -1);
                dateRange = new DateRange(DateUtil.getWeekFirstDay(before), DateUtil.getWeekFirstDay(current));
                break;
            case WEEK:
                dateRange = new DateRange(DateUtil.getBeginDayOfWeek(), DateUtil.getEndDayOfWeek());
                break;
            case NEARLYMONTH:
                before = DateUtil.addDays(current, -30);
                dateRange = new DateRange(before, current);
                break;
            case LASTMONTH:
                before = DateUtil.addDays(DateUtil.getMonthFirstDay(current), -1);
                dateRange = new DateRange(DateUtil.getMonthFirstDay(before), DateUtil.getDayEndTime(DateUtil.getMonthLastDay(before)));
                break;
            case MONTH:
                dateRange = new DateRange(DateUtil.getMonthFirstDay(current), DateUtil.getDayEndTime(DateUtil.getMonthLastDay(current)));
                break;
            case NEARLYTWOMONTHS:
                before = DateUtil.addMonths(current, -2);
                dateRange = new DateRange(before, current);
                break;
            case NEARLYQUARTER:
                before = DateUtil.addMonths(current, -3);
                dateRange = new DateRange(before, current);
                break;
            case LASTQUARTER:
                try {
                    before = DateUtil.addMonths(current, -3);
                    dateRange = getQuarter(before);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case QUARTER:
                try {
                    dateRange = getQuarter(current);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case NEARLYYEAR:
                before = DateUtil.addYears(DateUtil.getMonthFirstDay(current), -1);
                dateRange = new DateRange(before, current);
                break;
            case LASTYEAR:
                before = DateUtil.addYears(DateUtil.getBeginDayOfYear(), -1);
                current = DateUtil.addYears(DateUtil.getEndDayOfYear(), -1);
                dateRange = new DateRange(before, current);
                break;
            case YEAR:
                dateRange = new DateRange(DateUtil.getBeginDayOfYear(), DateUtil.getEndDayOfYear());
                break;
            default:
                throw new BusinessException("不支持的类型");
        }

        return dateRange;
    }

    private DateRange getQuarter(Date time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int quarterStartMonth = 0;
        int quarterEndMonth = 2;
        int quarterEndMonthLastDay = 31;
        if (month >= 4 && month <= 6) {
            quarterStartMonth = 3;
            quarterEndMonth = 5;
            quarterEndMonthLastDay = 30;
        } else if (month >= 7 && month <= 9) {
            quarterStartMonth = 6;
            quarterEndMonth = 8;
            quarterEndMonthLastDay = 30;
        } else if (month >= 10 && month <= 12) {
            quarterStartMonth = 9;
            quarterEndMonth = 11;
            quarterEndMonthLastDay = 31;
        }
        cal.set(year, quarterStartMonth, 1, 0, 0, 0);

        Date startTime = formatter.parse(formatter.format(cal.getTime()));
        cal.set(year, quarterEndMonth, quarterEndMonthLastDay, 23, 59, 59);
        Date endTime = formatter.parse(formatter.format(cal.getTime()));

        return new DateRange(startTime, endTime);
    }
}
