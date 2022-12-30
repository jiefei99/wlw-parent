package com.jike.wlw.core.support.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ServerMQTT {

    // tcp://MQTT安装的服务器地址：MQTT定义的端口号
    public static final String HOST = "tcp://a1dg6xBsM9h.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
    // 定义一个主题
    public static final String TOPIC = "/a1dg6xBsM9h/wyxTestDemo001/user/get";
    // 定义MQTT的id，可以再MQTT服务配置中指定
    private static final String clientId = "a1dg6xBsM9h.wyxTestDemo001|securemode=2,signmethod=hmacsha1,timestamp=1655433840762|";

    private MqttClient client;
    private MqttTopic topic;
    private String userName = "wyxTestDemo001&a1dg6xBsM9h";
    private String password = "4583F796E6DDB950D5BD7532A0306CBEA653229D";

    private MqttMessage message;

    /**
     * 构造函数
     *
     * @throws MqttException
     */
    public ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientId, new MemoryPersistence());
        connect();
    }

    /**
     * 用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(30);
        try {
            client.setCallback(new PushCallBack(client, options));
            client.connect(options);

            topic = client.getTopic(TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! " + token.isComplete());
    }

    /**
     * 启动入口
     *
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws MqttException {
        ServerMQTT server = new ServerMQTT();

        server.message = new MqttMessage();
        server.message.setQos(1);
        server.message.setRetained(true);
        server.message.setPayload("hello,home/garden/fountain".getBytes());
        server.publish(server.topic, server.message);
        System.out.println(server.message.isRetained() + "------retained状态");
    }
}
