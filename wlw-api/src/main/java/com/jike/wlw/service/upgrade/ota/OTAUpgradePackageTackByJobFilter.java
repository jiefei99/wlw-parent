/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月13日 23:21 - ASUS - 创建。
 */
package com.jike.wlw.service.upgrade.ota;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("OTA升级包请求参数")
public class OTAUpgradePackageTackByJobFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -7698859554336354121L;

    @ApiModelProperty("租户")
    private String tenantId;
    @ApiModelProperty("实例Id")
    private String iotInstanceId;
    @ApiModelProperty("设备名称")
    private List<String> deviceNames;
    @ApiModelProperty("设备名称")
    private String deviceNameEq;
    @ApiModelProperty("升级批次ID")
    private String jobId;
    @ApiModelProperty("升级批次ID")
    private OTAUpgradePackageTaskStatusType taskStatus;
}
