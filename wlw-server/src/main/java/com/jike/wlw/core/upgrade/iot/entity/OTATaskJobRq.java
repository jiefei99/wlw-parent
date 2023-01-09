package com.jike.wlw.core.upgrade.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @title: OTATaskJobRq
 * @Author RS
 * @Date: 2023/1/9 15:04
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("升级作业job请求参数")
public class OTATaskJobRq implements Serializable {
    private static final long serialVersionUID = 7453222224272538946L;

    @ApiModelProperty("实例ID")
    private String iotInstanceId;
    @ApiModelProperty("所属产品的ProductKey")
    private String productKey;
    @ApiModelProperty("升级批次ID")
    private String jobId;
    @ApiModelProperty("每页设备升级作业数量，最大限制：100。")
    private Integer pageSize;
    @ApiModelProperty("每页设备升级作业数量，页数从1开始排序。")
    private Integer currentPage;
    @ApiModelProperty("升级包Id")
    private String firmwareId;
    @ApiModelProperty("设备名称")
    private String deviceName;
    @ApiModelProperty("设备名称列表")
    private List<String> deviceNames;
    @ApiModelProperty("升级状态")
    private String taskStatus;
}


