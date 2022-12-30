package com.jike.wlw.core.support.emqx;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MqttPushClient
 * @Description: TODO
 * @Author: liujianfu
 * @Date: 2021/08/16 14:48:38
 * @Version: V1.0
 **/
@Component
public class EmqxClient {
    private static final Logger logger = LoggerFactory.getLogger(EmqxClient.class);

    private static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        EmqxClient.client = client;
    }

    /**
     * 客户端连接
     *
     * @param host      ip+端口
     * @param clientID  客户端Id
     * @param username  用户名
     * @param password  密码
     * @param timeout   超时时间
     * @param keepalive 保留数
     */
    public void connect(String host, String clientID, String username, String password, int timeout,
        int keepalive, String mqttTopic) {
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
            // 设置自动重连
            options.setAutomaticReconnect(true);
            EmqxClient.setClient(client);
            try {
                // 此处使用的MqttCallbackExtended类而不是MqttCallback，是因为如果emq服务出现异常导致客户端断开连接后，重连后会自动调用connectComplete方法
                client.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        System.out.println("连接完成...");
                        try {
                            // 重连后要自己重新订阅topic，这样emq服务发的消息才能重新接收到，不然的话，断开后客户端只是重新连接了服务，并没有自动订阅，导致接收不到消息
                            client.subscribe(mqttTopic);
                            logger.info("订阅成功");
                        } catch (Exception e) {
                            logger.info("订阅出现异常:{}", e);
                        }
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("失去连接....");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        // subscribe后得到的消息会执行到这里面
                        String content = new String(message.getPayload());
                        System.out.println("接收消息主题 : " + topic);
                        System.out.println("接收消息Qos : " + message.getQos());
                        System.out.println("接收消息内容 : " + content);
                        System.out.println("接收消息 : " + message.getId());
                        // 处理数据
                        client.messageArrivedComplete(message.getId(),message.getQos());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("deliveryComplete....");
                    }
                });
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布
     *
     * @param qos         连接方式
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = EmqxClient.getClient().getTopic(topic);
        if (null == mTopic) {
            logger.error("topic not exist");
        }
        MqttDeliveryToken token;

        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos) {
        logger.info("==============开始订阅主题=========" + topic);
        try {
            EmqxClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
