package com.jike.wlw.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/10/29- zhengzhoudong - 创建。
 */
@Setter
@Getter
public class SumAmount implements Serializable {
    private static final long serialVersionUID = -4215190811703963899L;

    @Autowired
    private BigDecimal sumAmount = BigDecimal.ZERO;

}
