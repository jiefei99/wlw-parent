/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月19日 1:40 - ASUS - 创建。
 */
package com.jike.wlw.service.equipment;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */
@Getter
@Setter
public class QueryDeviceGroupListFilter  extends AbstractQueryFilter {
    private static final long serialVersionUID = -7698859154276354122L;

    private String groupName;
    private String superGroupId;
    private String iotInstanceId;
    private List<String> groupTypes;
    private boolean allQueryFlag=false;
}
