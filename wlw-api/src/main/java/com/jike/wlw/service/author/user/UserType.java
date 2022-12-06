/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/3/28 19:17 - chenpeisi - 创建。
 */
package com.jike.wlw.service.author.user;

public enum UserType {

    EMPLOYEE("员工"), MEMBER("会员");

    private String caption;

    private UserType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}