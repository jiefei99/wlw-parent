package com.jike.wlw.service.author.org;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2022，所有权利保留。
 * <p>
 * 修改历史：
 * 2022/7/20 14:44- zhengzhoudong - 创建。
 */

/**
 * 组织类型
 */
public enum OrgType {

    SYSTEM("平台"),
    VENDOR("供应商"),
    PURCHASER("采购商"),
    STORE("店铺");

    private String caption;

    private OrgType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
