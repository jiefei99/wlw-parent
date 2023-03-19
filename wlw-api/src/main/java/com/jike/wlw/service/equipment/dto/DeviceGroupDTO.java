/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月19日 1:45 - ASUS - 创建。
 */
package com.jike.wlw.service.equipment.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */
@Getter
@Setter
public class DeviceGroupDTO implements Serializable {
    public String groupDesc;
    public String groupId;
    public String groupName;
    public String groupType;
    public Date created;
}
