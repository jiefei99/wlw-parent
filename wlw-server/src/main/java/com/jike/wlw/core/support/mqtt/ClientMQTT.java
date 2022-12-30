package com.jike.wlw.core.support.mqtt;

import com.geeker123.rumba.commons.exception.BusinessException;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ClientMQTT {

    //    public static final String HOST = "tcp://118.31.189.96:11883"; //阿振搭建的MQTT
    public static final String HOST = "tcp://a1dg6xBsM9h.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
    public static final String TOPIC = "/a1dg6xBsM9h/wyxTestDemo001/user/get";
    public static final String clientId = "a1dg6xBsM9h.wyxTestDemo001|securemode=2,signmethod=hmacsha1,timestamp=1655433840762|";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "wyxTestDemo001&a1dg6xBsM9h";
    private String password = "4583F796E6DDB950D5BD7532A0306CBEA653229D"; //需要计算 //TODO 需要接入阿里云pwd计算方法

//    private ScheduledExecutorService scheduler;

    private void start() {
        try {
            //host为主机名，clientId即连接MQTT的客户端id，一般以唯一标识符表示，memoryPersistence设置clientId的保存形式，默认以内存保存
            client = new MqttClient(HOST, clientId, new MemoryPersistence());
            //MQTT连接设置
            options = new MqttConnectOptions();
            //设置是否清空session，这里如果设置为false，则表示服务器会保留客户端的连接记录，这里设置为true，表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接参数
            options.setUserName(userName);
            //计算签名
//            String signContent = "authId=" + accessKey + "&timestamp=" + timeStamp;
//            String sign = doSign(signContent, "", "hmacsha1");
//            options.setPassword(sign.toCharArray());
            options.setPassword(password.toCharArray());
            //设置超时时间，单位s
            options.setConnectionTimeout(10);
            //设置绘画心跳时间，单位s，服务器会每隔1.5*20s的时间向客户端发送消息判断客户端是否在线，但是这个方法没有重连机制
//            options.setKeepAliveInterval(20);
            options.setKeepAliveInterval(30); //阿里的心跳时间取值范围为[30s,1200s]，建议取值300s以上
            //设置回调
            client.setCallback(new PushCallBack(client, options));
            MqttTopic topic = client.getTopic(TOPIC);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法，设置最终端口的通知信息
            options.setWill(topic, "close".getBytes(), 2, true);

            client.connect(options);
            //订阅消息
            int[] Qos = {1};
            String[] topic1 = {TOPIC};
            client.subscribe(topic1, Qos);
        } catch (Exception e) {
//            log.error(e.getMessage(),e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws MqttException {
        ClientMQTT client = new ClientMQTT();
        client.start();
    }

    private static String doSign(String toSignString, String secret, String signMethod) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), signMethod);
        Mac mac = Mac.getInstance(signMethod);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(toSignString.getBytes());
        return Base64.encodeBase64String(rawHmac);
    }
}
