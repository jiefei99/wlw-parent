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
public class BatchUpdateDeviceNicknameRq implements Serializable {
    private static final long serialVersionUID = -8580030149082080444L;

    @ApiModelProperty("设备集合")
    public List<DeviceNicknameInfo> deviceNicknameInfo;
    @ApiModelProperty("实例ID")
    public String iotInstanceId;

    public static class DeviceNicknameInfo {
        @ApiModelProperty("所属产品ProductKey")
        public String productKey;
        @ApiModelProperty("设备名称")
        public String deviceName;
        @ApiModelProperty("设备备注名称")
        public String nickname;
        @ApiModelProperty("设备ID")
        public String iotId;

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIotId() {
            return iotId;
        }

        public void setIotId(String iotId) {
            this.iotId = iotId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }
    }
}



