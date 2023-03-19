/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月18日 21:57 - ASUS - 创建。
 */
package com.jike.wlw.service.equipment;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */
@Getter
@Setter
public class EquipmentSqlFilter implements Serializable {
    private static final long serialVersionUID = -3463321726813052611L;

    private String sql;
    private String productKey;
    private String iotInstanceId;
    private int pageSize=50;
}
