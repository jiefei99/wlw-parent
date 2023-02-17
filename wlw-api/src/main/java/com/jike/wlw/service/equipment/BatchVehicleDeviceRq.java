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
public class BatchVehicleDeviceRq implements Serializable {
    private static final long serialVersionUID = -1033333550444873947L;

    @ApiModelProperty("设备集合")
    public List<DeviceList> deviceList;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;
    @ApiModelProperty("所属产品ProductKey")
    public String productKey;

    public static class DeviceList {
        @ApiModelProperty("设备ID")
        public String deviceId;
        @ApiModelProperty("设备模型")
        public String deviceModel;
        @ApiModelProperty("制造商")
        public String manufacturer;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }
    }

}
