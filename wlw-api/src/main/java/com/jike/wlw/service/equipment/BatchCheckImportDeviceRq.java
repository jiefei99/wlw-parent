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
@ApiModel
public class BatchCheckImportDeviceRq implements Serializable {
    private static final long serialVersionUID = -8580030149082080444L;

    @ApiModelProperty("设备集合")
    public List<DeviceList> deviceList;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;

    public static class DeviceList {
        @ApiModelProperty("设备名称")
        public String deviceName;
        @ApiModelProperty("设备密钥")
        public String deviceSecret;
        @ApiModelProperty("序列号")
        public String sn;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceSecret() {
            return deviceSecret;
        }

        public void setDeviceSecret(String deviceSecret) {
            this.deviceSecret = deviceSecret;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
    }
}



