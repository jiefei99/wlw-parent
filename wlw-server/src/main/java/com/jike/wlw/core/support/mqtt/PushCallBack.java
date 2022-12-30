package com.jike.wlw.core.support.mqtt;

import com.geeker123.rumba.commons.exception.BusinessException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PushCallBack implements MqttCallback {

    private MqttClient mqttClient;
    private MqttConnectOptions options;

    public PushCallBack(MqttClient mqttClient, MqttConnectOptions options) {
        this.mqttClient = mqttClient;
        this.options = options;
    }

    @Override
    public void connectionLost(Throwable throwable) throws BusinessException {
        //连接丢失后，一般在这里重连
        System.out.println("连接断开，尝试重连");
        try {
            mqttClient.connect(options);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        //subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主体：" + topic);
        System.out.println("接收消息Qos：" + mqttMessage.getQos());
        System.out.println("接收消息内容：" + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete-----------" + iMqttDeliveryToken.isComplete());
    }

//    @Override
//    public void command(EquipmentCommandRq commandRq, String operator) throws BusinessException {
//        try {
//            if (StringUtil.isNullOrBlank(commandRq.getType())) {
//                throw new BusinessException("类型不能为空");
//            }
//
//            Equipment equipment = get(commandRq.getId());
//            if (equipment == null) {
//                throw new BusinessException("指定设备不存在或已删除");
//            }
//
//            // 站点id，根据实际站点获取对应id
//            String regionId = aliIotProperties.getRegionId();
//            // 用户账号AccessKey
//            String accessKeyID = aliIotProperties.getAccessKey();
//            // 用户账号AccesseKeySecret
//            String accessKeySecret = aliIotProperties.getAccessSecret();
//            //iot InstanceId
//            String iotInstanceId = aliIotProperties.getInstanceId();
//            // 设备名字deviceName
//            String deviceName = equipment.getEquipmentId();
//            // 产品productKey
//            String productKey = "gvf3ulWPlb3";
//            for (Point point : equipment.getPointList()) {
//                if ("productKey".equals(point.getPointName())) {
//                    productKey = point.getValue().toString();
//                    break;
//                }
//            }
//            //标识符
//            String apologize = null;
//            if ("addCornTempSet".equals(commandRq.getType())) {
//                apologize = "addCornTemp";
//            } else if ("sugaringTempSet".equals(commandRq.getType())) {
//                apologize = "sugaringTemp";
//            } else if ("tempMaxSet".equals(commandRq.getType())) {
//                apologize = "tempMax";
//            } else {
//                throw new BusinessException("未知标识符");
//            }
//
//            // 获取服务端请求客户端
//            DefaultAcsClient client = null;
//            try {
//                IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyID, accessKeySecret);
//                DefaultProfile.addEndpoint(regionId, regionId, "Iot", "iot." + regionId + ".aliyuncs.com");
//                client = new DefaultAcsClient(profile);
//            } catch (Exception e) {
//                System.out.println("create OpenAPI Client failed !! exception:" + e.getMessage());
//            }
//
//            // 填充服务调用的参数
//            InvokeThingServiceRequest request = new InvokeThingServiceRequest();
//            request.setProductKey(productKey); // 设备证书之productKey
//            request.setDeviceName(deviceName); // 设备证书之deviceName
//            request.setIdentifier(commandRq.getType()); // 要调用的服务标识符，取决于服务定义
//            JSONObject json = new JSONObject(); // 构造服务入参，服务入参是一个JSON String
//            json.put(apologize, commandRq.getTemperature()); // 取决于服务定义，取值要符合服务定义时配置的参数规格
//            request.setArgs(json.toString());
//            request.setIotInstanceId(iotInstanceId);
//
//            // 获得服务调用响应
//            InvokeThingServiceResponse response = client.getAcsResponse(request);
//            if (response == null) {
//                System.out.println("调用服务异常");
//                throw new BusinessException("调用服务异常");
//            }
//            System.out.println("requestId:" + response.getRequestId());
//            System.out.println("code:" + response.getCode());
//            System.out.println("success:" + response.getSuccess());
//            System.out.println("error message:" + response.getErrorMessage());
//            if (response != null && response.getSuccess()) { // 服务调用成功，仅代表发送服务指令的成功，不代表执行服务本身是否成功
//                System.out.println("服务调用成功");
//                System.out.println("消息id：" + response.getData().getMessageId());
//                System.out.println("服务返回结果：" + response.getData().getResult()); // 仅同步服务有result
//            } else {
//                System.out.println("服务调用失败");
//                throw new BusinessException("服务调用失败：" + response.getErrorMessage());
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new BusinessException(e.getMessage(), e);
//        }
//    }
}
