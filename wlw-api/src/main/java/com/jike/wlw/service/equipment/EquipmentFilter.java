package com.jike.wlw.service.equipment;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("设备查询条件")
public class EquipmentFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -3463321726813052611L;

    @ApiModelProperty("租户编号等于")
    private String tenantIdEq;
    @ApiModelProperty("编号等于")
    private String idEq;
    @ApiModelProperty("名称等于")
    private String nameEq;
    @ApiModelProperty("产品的ProductKey等于")
    private String productKeyEq;
    @ApiModelProperty("状态等于")
    private EquipmentStatus statusEq;
    @ApiModelProperty("所属资源组ID")
    public String resourceGroupIdEq;
    @ApiModelProperty("是否删除")
    public Boolean deleted;

    @ApiModelProperty("编号在之中")
    private List<String> idIn;
    @ApiModelProperty("产品的ProductKey在之中")
    private List<String> productKeyIn;
    @ApiModelProperty("状态在之中")
    private List<String> statusIn;
    @ApiModelProperty("所属资源组ID")
    public List<String> resourceGroupIdIn;

    @ApiModelProperty("名称类似")
    private String nameLike;
}
