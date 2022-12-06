package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2021，所有权利保留。
 * <p>
 * 修改历史：
 * 2021/10/26 17:25- zhengzhoudong - 创建。
 */
@ApiModel("时间范围类型")
public enum  DateRangeType {

    //配合数据库  %H(00-23) ；%k(0-23) ；%h(01-12) ； %l (1-12)
    HOUR("小时"),
    //配合数据库  %d月的天(00-31) ；%e月的天(0-31)
    DAY("天"),
    //配合数据库   %M月名 ；%m月(00-12) ；%c月(0-12)
    MONTH("月"),
    //配合数据库   %Y 年,4位 ；%y年，2位
    YEAR("年");

    private String caption;

    private DateRangeType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
