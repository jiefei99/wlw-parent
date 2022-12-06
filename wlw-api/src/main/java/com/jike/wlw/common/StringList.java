package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2021，所有权利保留。
 * <p>
 * 修改历史：
 * 2021/11/5 16:33- sufengjia - 创建。
 */
@Getter
@Setter
@ApiModel("字符串数组接收对象")
public class StringList implements Serializable {
    private static final long serialVersionUID = -8129611404430843354L;

    @ApiModelProperty("字符串")
    private String str;

    public List<String> coverToStringList(List<StringList> stringLists) {
        List<String> list = new ArrayList<>();

        if (CollectionUtils.isEmpty(stringLists)) {
            return list;
        }

        for (StringList stringList : stringLists) {
            list.add(stringList.getStr());
        }

        return list;
    }
}
