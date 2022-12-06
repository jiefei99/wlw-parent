package com.jike.wlw.service.support.oss;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/5/15 15:04- sufengjia - 创建。
 */
@Getter
@Setter
public class OssResult implements Serializable {
    private static final long serialVersionUID = -344913610190467624L;

    private String accessid;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private String expire;

}
