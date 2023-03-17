package com.jike.wlw.service.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wza
 * @create 2023/2/17
 */
@Getter
@Setter
@ApiModel("设备名称批量校验请求参数")
public class BatchCheckDeviceNamesRq implements Serializable {
    private static final long serialVersionUID = -8580030149082080444L;

    @ApiModelProperty("设备集合") // deviceName 与 deviceNameList必须传入一种。若您同时传入deviceName与deviceNameList，则以deviceNameList为准。
    public List<DeviceNameList> deviceNameList;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;
    @ApiModelProperty("deviceName")
    public List<String> deviceName;

    public static class DeviceNameList {
        @ApiModelProperty("设备名称")
        public String deviceName;
        @ApiModelProperty("设备备注名称")
        public String deviceNickname;

        public String getDeviceNickname() {
            return deviceNickname;
        }

        public void setDeviceNickname(String deviceNickname) {
            this.deviceNickname = deviceNickname;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }
    }
}



